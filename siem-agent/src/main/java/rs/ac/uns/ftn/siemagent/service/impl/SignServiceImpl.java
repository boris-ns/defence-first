package rs.ac.uns.ftn.siemagent.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemagent.Constants.Constants;
import rs.ac.uns.ftn.siemagent.model.Log;
import rs.ac.uns.ftn.siemagent.repository.Keystore;
import rs.ac.uns.ftn.siemagent.service.SignService;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

@Service
public class SignServiceImpl implements SignService {

    @Value("${keystore.password}")
    private String keyStorePassword;

    @Autowired
    private Keystore keystore;
    private PrivateKey myPrivateKey;


    @Override
    public void singLog(Log l) throws Exception {
        myPrivateKey = keystore.readPrivateKey(Constants.KEY_PAIR_ALIAS, keyStorePassword);

        String data = l.toString();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(myPrivateKey);
        signature.update(data.getBytes("UTF-8"));
        byte[] signatureBytes = signature.sign();
        String signatureString = Base64.getEncoder().encodeToString(signatureBytes);
        l.setSignature(signatureString);
    }
}
