package rs.ac.uns.ftn.siemagent.service;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

public interface KeyPairGeneratorService {

    KeyPair generateKeyPair();

    SecretKey generateSimetricKey() throws NoSuchAlgorithmException;
}
