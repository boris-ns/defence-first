package rs.ac.uns.ftn.pkiservice.repository.impl;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.pkiservice.configuration.MyKeyStore;
import rs.ac.uns.ftn.pkiservice.exception.exceptions.ApiRequestException;
import rs.ac.uns.ftn.pkiservice.exception.exceptions.ResourceNotFoundException;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.repository.KeyStoreRepository;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;

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
    public Certificate readCertificate(String alias) {
        KeyStore ks = myKeyStore.getKeystore();
        Certificate cert = null;
        try {
            if (ks.isKeyEntry(alias)) {
                cert = ks.getCertificate(alias);
            }
            if (ks.isCertificateEntry(alias)) {
                cert = ks.getCertificate(alias);
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("Certificate doesn't exist");
        }
        return cert;
    }

    // @TODO: Implementirati ovo
    @Override
    public Certificate[] readAll() {
        KeyStore ks = myKeyStore.getKeystore();

        try {
            // @TODO: Ovde bi trebalo uzeti chain od root sertifikata
            // proveriti onda da li ce elementi tog chaina imati i svoje chain elemente
            // ili ce se morati rekurzivno proci kroz sve sertifikate da bi se pokupili
            // svi elementi lanca
            return ks.getCertificateChain("root");
        } catch (KeyStoreException e) {
            throw new ApiRequestException("Error while loading keystore");
        }
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
        if(privateKey != null) {
            myKeyStore.getKeystore().setKeyEntry(alias, privateKey, password, new Certificate[]{certificate});
        }else {
            myKeyStore.getKeystore().setCertificateEntry(alias, certificate);
        }
        saveKeyStore();
    }

    @Override
    public void saveKeyStore() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
        myKeyStore.getKeystore().store(new FileOutputStream(MyKeyStore.FILE_PATH),  MyKeyStore.PASSWORD);
    }
}
