package rs.ac.uns.ftn.siemcentar.service;

import rs.ac.uns.ftn.siemcentar.dto.response.TokenDTO;

import javax.security.auth.x500.X500Principal;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public interface CertificateService {

    void installCertificateFromFile(String path) throws CertificateException, Exception;

    X509Certificate findMyCertificate() throws Exception;

    String buildCertificateRequest(KeyPair certKeyPair, PrivateKey signCerPrivateKey, Boolean renewal) throws Exception;

    X500Principal buildSertificateSubjetPrincipal();

    boolean checkSertificate(X509Certificate certificate);

    void saveKeyPair(KeyPair keyPair, Boolean renewal) throws Exception;

    X509Certificate getCertificateBySerialNumber(String serialNumber, TokenDTO token) throws Exception;

    String sendReplaceCertificateRequest(TokenDTO token) throws Exception;

    String sendRequestForCertificate(TokenDTO token) throws Exception;
}
