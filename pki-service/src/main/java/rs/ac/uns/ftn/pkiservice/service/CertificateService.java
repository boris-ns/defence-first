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
            NoSuchProviderException, KeyStoreException, IOException, UnrecoverableKeyException;

    X509Certificate generateCertificate(Constants.CERT_TYPE certType)
            throws CertificateException, UnrecoverableKeyException,
                NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, SignatureException,
                InvalidKeyException;

    Certificate getCertificateByAlias(String alias);

    void writeCertificateToKeyStore(X509Certificate cert, Constants.CERT_TYPE certType, PrivateKey pk)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException;
}
