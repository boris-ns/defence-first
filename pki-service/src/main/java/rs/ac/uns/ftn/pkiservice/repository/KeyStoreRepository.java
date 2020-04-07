package rs.ac.uns.ftn.pkiservice.repository;

import rs.ac.uns.ftn.pkiservice.models.IssuerData;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Collection;
import java.util.List;

public interface KeyStoreRepository {

    IssuerData loadIssuer(String alias) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException;

    Certificate readCertificate(String alias);

    Certificate[] readCertificateChain(String alias);

    List<Certificate> readAll();

    PrivateKey readPrivateKey(String alias, String pass) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException;

    void writeKeyEntry(String alias, PrivateKey privateKey, Certificate[] certificates) throws
            KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException;

    void writeCertificateEntry(String alias, Certificate certificate) throws
            KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException;

    void saveKeyStore() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException;

}
