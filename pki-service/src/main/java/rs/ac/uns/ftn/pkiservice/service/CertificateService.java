package rs.ac.uns.ftn.pkiservice.service;

import rs.ac.uns.ftn.pkiservice.models.IssuerData;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public interface CertificateService {

    X509Certificate findById(String id);

    X509Certificate findCertificateByAlias(String alias) throws NoSuchAlgorithmException, CertificateException,
            NoSuchProviderException, KeyStoreException, IOException, UnrecoverableKeyException;

    IssuerData findIssuerByAlias(String alias) throws NoSuchAlgorithmException, CertificateException,
            NoSuchProviderException, KeyStoreException, IOException, UnrecoverableKeyException;

    X509Certificate generateCertificate() throws CertificateException, UnrecoverableKeyException,
            NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, SignatureException,
            InvalidKeyException;
}
