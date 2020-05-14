package rs.ac.uns.ftn.pkiservice.repository.impl;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.pkiservice.configuration.MyKeyStore;
import rs.ac.uns.ftn.pkiservice.exception.exceptions.ApiRequestException;
import rs.ac.uns.ftn.pkiservice.exception.exceptions.ResourceNotFoundException;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.repository.KeyStoreRepository;

import javax.security.auth.x500.X500PrivateCredential;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import static rs.ac.uns.ftn.pkiservice.configuration.MyKeyStore.*;


@Repository
public class KeyStoreRepositoryImpl implements KeyStoreRepository {

    @Autowired
    private MyKeyStore myKeyStore;

    @Autowired @Qualifier("archiveKeyStore")
    private KeyStore archiveKeyStore;

    @Override
    public IssuerData loadIssuer(String alias) throws KeyStoreException, CertificateException, NoSuchAlgorithmException,
            UnrecoverableKeyException {
        KeyStore keyStore = myKeyStore.getKeystore();
        //Iscitava se sertifikat koji ima dati alias
        Certificate cert = keyStore.getCertificate(alias);
        //Iscitava se privatni kljuc vezan za javni kljuc koji se nalazi na sertifikatu sa datim aliasom
        PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, myKeyStore.getKEYSTORE_PASSWORD());

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
    public List<String> readAliases() {
        KeyStore ks = myKeyStore.getKeystore();
        try {
            return Collections.list(ks.aliases());
        } catch (KeyStoreException e) {
            throw new ApiRequestException("Error while loading keystore");
        }
    }

    @Override
    public List<Certificate[]> listChain() {
        KeyStore ks = myKeyStore.getKeystore();
        try {
            List<Certificate[]> chains = new ArrayList<>();
            Enumeration<String> aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                chains.add(readCertificateChain(alias));
            }
            return chains;
        } catch (KeyStoreException e) {
            throw new ApiRequestException("Error while loading keystore");
        }
    }

    @Override
    public List<X500PrivateCredential> readCertificateAndAliasForRootAndIntermediate() {
        KeyStore ks = myKeyStore.getKeystore();
        try {
            List<X500PrivateCredential> credentials = new ArrayList<>();
            Enumeration<String> aliases = ks.aliases();
            String alias;
            while (aliases.hasMoreElements()) {
                alias = aliases.nextElement();
                X509Certificate c = (X509Certificate) readCertificate(alias);
                boolean[] keyusage = c.getKeyUsage();
                if(keyusage!= null && keyusage[5] && keyusage[6]) {
                    credentials.add(new X500PrivateCredential(c, readPrivateKey(alias) ,alias));
                }
            }
            return credentials;
        } catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException e) {
            throw new ApiRequestException("Error while loading keystore");
        }
    }

    @Override
    public PrivateKey readPrivateKey(String alias) throws KeyStoreException, UnrecoverableKeyException,
            NoSuchAlgorithmException {
        KeyStore ks = myKeyStore.getKeystore();

        if (ks.isKeyEntry(alias)) {
            return (PrivateKey) ks.getKey(alias, myKeyStore.getKEYSTORE_PASSWORD());
        }
        return null;
    }

    @Override
    public void writeKeyEntry(String alias, PrivateKey privateKey, Certificate[] certificates) throws
            KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        myKeyStore.getKeystore().setKeyEntry(alias, privateKey, myKeyStore.getKEYSTORE_PASSWORD(), certificates);
        saveKeyStore();
    }


    @Override
    public void saveKeyStore() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
        myKeyStore.getKeystore().store(new FileOutputStream(myKeyStore.getKEYSTORE_FILE_PATH()), myKeyStore.getKEYSTORE_PASSWORD());
    }

    @Override
    public void writeKeyEntryToArchive(String alias, PrivateKey privateKey, Certificate[] certificates) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        archiveKeyStore.setKeyEntry(alias, privateKey, myKeyStore.getKEYSTORE_ARCHIVE_PASSWORD(), certificates);
        archiveKeyStore.store(new FileOutputStream(myKeyStore.getKEYSTORE_ARCHIVE_FILE_PATH()),
                myKeyStore.getKEYSTORE_ARCHIVE_PASSWORD());
    }

    @Override
    public void deleteEntry(String alias) throws KeyStoreException {
        myKeyStore.getKeystore().deleteEntry(alias);
    }


}
