package rs.ac.uns.ftn.pkiservice.service;

import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;

import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

public interface CertificateService {

    List<X509Certificate> findAll();

    X509Certificate findCertificateByAlias(String alias);

    IssuerData findIssuerByAlias(String alias) throws NoSuchAlgorithmException, CertificateException,
            KeyStoreException, IOException, UnrecoverableKeyException;

    X509Certificate generateCertificateIntermediate(String subjectName, String issuerAlias) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException;

    Certificate getCertificateByAlias(String alias);

    Certificate[] getCertificateChainByAlias(String alias);

    void writeCertificateToKeyStore(X509Certificate cert, Constants.CERT_TYPE certType, PrivateKey pk)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException;

    List<X509Certificate> findAllRequests();
}