package rs.ac.uns.ftn.siemcentar.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemcentar.constants.Constants;
import rs.ac.uns.ftn.siemcentar.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemcentar.messaging.WebSocketProducer;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.repository.LogRepository;
import rs.ac.uns.ftn.siemcentar.service.LogService;
import rs.ac.uns.ftn.siemcentar.service.OCSPService;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private OCSPService ocspService;

    @Autowired
    private AuthService authService;

    @Autowired
    private Keystore keystore;

    @Value("${keystore.password}")
    private String keyStorePassword;

    @Autowired
    private CipherService cipherService;

    @Autowired
    private WebSocketProducer socketProducer;

    @Override
    public void saveLogs(List<Log> logs) {
        logRepository.saveAll(logs);
        logs.forEach(log -> socketProducer.sendLog(log));
    }

    @Override
    public List<Log> findAll() {
        return logRepository.findAll();
    }

    @Override
    public SecretKey processPreMasterSecret(X509Certificate clientCertificate, byte[] secretKey) throws Exception {
        SecretKey key = null;
        TokenDTO token = authService.login();
        OCSPReq request = ocspService.generateOCSPRequest(clientCertificate);
        OCSPResp response = ocspService.sendOCSPRequest(request);
        boolean val = ocspService.processOCSPResponse(request, response);

        if(val) {
            System.out.println("validan je sertifikat klijenta: " + clientCertificate.getSerialNumber());
            //@TODO: kako ovaj kljuc sad da dodamo u sesiju da bude globalno dostupan da mozemo
            // podatke sa njim da citamo koje ce da zakriptuje klijent...
             key = processSimetricKey(secretKey);
        }
        else {
            System.out.println("Odbijam komunikaciju sa klijentom: " + clientCertificate.getSerialNumber());
        }
        return key;
    }

    @Override
    public byte[] encriptSimetricKeyWithMyKey(SecretKey key) throws Exception {
        X509Certificate myCertificate = (X509Certificate) keystore.readMyCertificate();
        return cipherService.encrypt(myCertificate.getPublicKey(), key.getEncoded());
    }

    @Override
    public byte[] encriptSimetricKey(SecretKey secretKey, PublicKey publicKey) throws Exception {
        return cipherService.encrypt(publicKey, secretKey.getEncoded());
    }

    @Override
    public byte[] decriptWitmMyPrivate(byte[] data) throws Exception{
        PrivateKey myPrivateKey = keystore.readPrivateKey(Constants.KEY_PAIR_ALIAS, keyStorePassword);
        return cipherService.decrypt(myPrivateKey, data);
    }

    @Override
    public ArrayList<String> convertLogsFromByte(byte[] logs) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(logs);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            return (ArrayList<String>)o;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    public SecretKey processSimetricKey(byte[] criptedSimetricKey) throws Exception{
        byte[] decirptedSimetricKey  = this.decriptWitmMyPrivate(criptedSimetricKey);
        SecretKey originalKey = new SecretKeySpec(decirptedSimetricKey, 0, decirptedSimetricKey.length, "AES");
        return originalKey;
    }


}
