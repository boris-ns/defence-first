package rs.ac.uns.ftn.pkiservice.repository;

import rs.ac.uns.ftn.pkiservice.models.IssuerData;

import javax.security.auth.x500.X500PrivateCredential;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface KeyStoreRepository {

    IssuerData loadIssuer(String alias) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException;

    Certificate readCertificate(String alias);

    Certificate[] readCertificateChain(String alias);

    List<Certificate> readAll();

    List<Certificate[]> listChain();

    List<X500PrivateCredential> readCertificateAndAliasForRootAndIntermediate();

    PrivateKey readPrivateKey(String alias) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException;

    void writeKeyEntry(String alias, PrivateKey privateKey, Certificate[] certificates) throws
            KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException;

    void saveKeyStore() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException;

    void writeKeyEntryToArchive(String alias, PrivateKey privateKey, Certificate[] certificates) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException;

    void deleteEntry(String alias) throws KeyStoreException;
}
