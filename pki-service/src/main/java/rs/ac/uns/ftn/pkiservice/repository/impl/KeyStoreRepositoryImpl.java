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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static rs.ac.uns.ftn.pkiservice.constants.Constants.*;


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
        PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, KEYSTORE_PASSWORD);

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
            throw new ResourceNotFoundException("Certificate doesn't exist");
        }
        return cert;
    }

    @Override
    public Certificate[] readCertificateChain(String alias) {
        KeyStore ks = myKeyStore.getKeystore();
        Certificate[] certificates = null;
        try {
            if (ks.isKeyEntry(alias)) {
                certificates = ks.getCertificateChain(alias);
            }
        } catch (KeyStoreException e) {
            throw new ResourceNotFoundException("Certificate doesn't exist");
        }
        return certificates;
    }

    @Override
    public List<Certificate> readAll() {
        KeyStore ks = myKeyStore.getKeystore();
        try {
            List<Certificate> certificates = new ArrayList<>();
            Enumeration<String> aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                certificates.add(readCertificate(aliases.nextElement()));
            }
            return certificates;
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
    public void writeKeyEntry(String alias, PrivateKey privateKey, Certificate[] certificates) throws
            KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        myKeyStore.getKeystore().setKeyEntry(alias, privateKey, KEYSTORE_PASSWORD, certificates);
        saveKeyStore();
    }

    @Override
    public void writeCertificateEntry(String alias, Certificate certificate) throws
            KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        myKeyStore.getKeystore().setCertificateEntry(alias, certificate);
        saveKeyStore();
    }

    @Override
    public void saveKeyStore() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
        myKeyStore.getKeystore().store(new FileOutputStream(KEYSTORE_FILE_PATH), KEYSTORE_PASSWORD);
    }
}