package rs.ac.uns.ftn.siemcentar.service.impl;

import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemcentar.constants.Constants;
import rs.ac.uns.ftn.siemcentar.dto.request.LogFilterDTO;
import rs.ac.uns.ftn.siemcentar.dto.response.LogDTO;
import rs.ac.uns.ftn.siemcentar.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.model.LogType;
import rs.ac.uns.ftn.siemcentar.repository.Keystore;
import rs.ac.uns.ftn.siemcentar.repository.LogRepository;
import rs.ac.uns.ftn.siemcentar.service.AuthService;
import rs.ac.uns.ftn.siemcentar.service.CipherService;
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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public void saveLogs(List<Log> logs) {
        logRepository.saveAll(logs);
    }

    @Override
    public List<Log> findAll() {
        return logRepository.findAll();
    }

    @Override
    public List<Log> searchAndFilter(LogFilterDTO filter) {
        List<Log> logs = logRepository.findAll();

        if (filter.getId() != null)
            logs = logs.stream().filter(log -> log.getId().equals(filter.getId())).collect(Collectors.toList());

        if (filter.getLogType() != null)
            logs = logs.stream().filter(log -> log.getLogType().equals(LogType.valueOf(filter.getLogType()))).collect(Collectors.toList());

        if (filter.getMessage() != null)
            logs = logs.stream().filter(log -> log.getMessage().toLowerCase().contains(filter.getMessage().toLowerCase())).collect(Collectors.toList());

        if (filter.getSource() != null)
            logs = logs.stream().filter(log -> log.getSource().toLowerCase().contains(filter.getSource().toLowerCase())).collect(Collectors.toList());


//        Date date = new GregorianCalendar(year, month - 1, day).getTime();

        // @TODO: dodati filtriranje po datumima

        return logs;
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
