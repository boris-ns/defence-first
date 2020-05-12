package rs.ac.uns.ftn.siemcentar.service.impl;

import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemcentar.constants.Constants;
import rs.ac.uns.ftn.siemcentar.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.repository.Keystore;
import rs.ac.uns.ftn.siemcentar.repository.LogRepository;
import rs.ac.uns.ftn.siemcentar.service.AuthService;
import rs.ac.uns.ftn.siemcentar.service.CipherService;
import rs.ac.uns.ftn.siemcentar.service.LogService;
import rs.ac.uns.ftn.siemcentar.service.OCSPService;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.List;

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
    public SecretKey processPreMasterSecret(X509Certificate clientCertificate, byte[] secretKey) throws Exception {
        SecretKey key = null;
        TokenDTO token = authService.login();
        OCSPReq request = ocspService.generateOCSPRequest(clientCertificate, token);
        OCSPResp response = ocspService.sendOCSPRequest(request, token);
        boolean val = ocspService.processOCSPResponse(request,response, token);

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

    public SecretKey processSimetricKey(byte[] criptedSimetricKey) throws Exception{
        byte[] decirptedSimetricKey  = this.decriptWitmMyPrivate(criptedSimetricKey);
        SecretKey originalKey = new SecretKeySpec(decirptedSimetricKey, 0, decirptedSimetricKey.length, "AES");
        return originalKey;
    }


}
