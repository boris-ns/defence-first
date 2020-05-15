package rs.ac.uns.ftn.siemagent.service.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemagent.service.KeyPairGeneratorService;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;

@Service
public class KeyPairGeneratorServiceImpl implements KeyPairGeneratorService {

    @Override
    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

}
