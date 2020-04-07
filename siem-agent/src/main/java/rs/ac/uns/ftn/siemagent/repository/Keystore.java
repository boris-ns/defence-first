package rs.ac.uns.ftn.siemagent.repository;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.siemagent.config.KeystoreConfiguration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

@Repository
public class Keystore {

    @Value("${keystore.filePath}")
    private String keyStoreFilePath;

    @Value("${keystore.password}")
    private String keyStorePassword;

    private KeystoreConfiguration keystoreConfiguration;
    private KeyStore keyStore;

    @Autowired
    public Keystore(KeystoreConfiguration keystoreConfiguration) {
        this.keystoreConfiguration = keystoreConfiguration;
        this.keyStore = keystoreConfiguration.getKeystore();
    }

    public PrivateKey readPrivateKey(String alias, String password) {
        try {
            if (keyStore.isKeyEntry(alias)) {
                PrivateKey pk = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
                return pk;
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(String alias, PrivateKey privateKey, char[] password, Certificate certificate) {
        try {
            keyStore.setKeyEntry(alias, privateKey, password, new Certificate[] {certificate});
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        saveKeyStore();
    }

    public void saveKeyStore() {
        try {
            keyStore.store(new FileOutputStream(keyStoreFilePath), keyStorePassword.toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }
}
