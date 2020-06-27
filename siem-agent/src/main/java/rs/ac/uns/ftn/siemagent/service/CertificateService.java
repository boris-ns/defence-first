package rs.ac.uns.ftn.siemagent.service;

import rs.ac.uns.ftn.siemagent.dto.response.TokenDTO;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public interface CertificateService {

    String buildCertificateRequest(KeyPair certKeyPair, PrivateKey signCerPrivateKey, Boolean renewal) throws Exception;

    X500Principal buildSertificateSubjetPrincipal();

    boolean checkSertificate(X509Certificate certificate);

    void saveKeyPair(KeyPair keyPair, Boolean renewal) throws Exception;

    X509Certificate getCertificateBySerialNumber(String serialNumber) throws Exception;

    void createReplaceCertificateRequest() throws Exception;

    void createRequestForCertificate() throws Exception;

    void installCertificateFromFile(Boolean renewal) throws CertificateException, Exception;

    X509Certificate findMyCertificate() throws Exception;

    InputStream fullStream(String certFile) throws IOException;
}
