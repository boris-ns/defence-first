package rs.ac.uns.ftn.pkiservice.repository.impl;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.pkiservice.configuration.MyKeyStore;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.repository.KeyStoreRepository;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Repository
public class KeyStoreRepositoryImpl implements KeyStoreRepository {

    @Autowired
    private MyKeyStore myKeyStore;

    @Override
    public IssuerData loadIssuer(String alias) throws KeyStoreException, CertificateException, NoSuchAlgorithmException,
            UnrecoverableKeyException {
        KeyStore keyStore = myKeyStore.getKeystore();
        //Iscitava se sertifikat koji ima dati alias
        Certificate cert = keyStore.getCertificate(alias);
        //Iscitava se privatni kljuc vezan za javni kljuc koji se nalazi na sertifikatu sa datim aliasom
        PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, MyKeyStore.PASSWORD);

        X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
        return new IssuerData(privKey, issuerName);
    }

    @Override
    public Certificate readCertificate(String alias) throws KeyStoreException {
        KeyStore ks = myKeyStore.getKeystore();

        if (ks.isKeyEntry(alias)) {
            Certificate cert = ks.getCertificate(alias);
            return cert;
        }
        return null;
    }

    @Override
    public PrivateKey readPrivateKey(String alias, String pass) throws KeyStoreException, UnrecoverableKeyException,
            NoSuchAlgorithmException {
        KeyStore ks = myKeyStore.getKeystore();

        if (ks.isKeyEntry(alias)) {
            PrivateKey pk = (PrivateKey) ks.getKey(alias, pass.toCharArray());
            return pk;
        }
        return null;
    }

    @Override
    public void write(String alias, PrivateKey privateKey, char[] password, Certificate certificate) throws
            KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        myKeyStore.getKeystore().setKeyEntry(alias, privateKey, password, new Certificate[] {certificate});
        saveKeyStore();
    }

    @Override
    public void saveKeyStore() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
        myKeyStore.getKeystore().store(new FileOutputStream(MyKeyStore.FILE_PATH),  MyKeyStore.PASSWORD);
    }
}
