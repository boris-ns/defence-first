package rs.ac.uns.ftn.siemagent.service;

import rs.ac.uns.ftn.siemagent.dto.response.TokenDTO;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;

public interface CertificateService {

    String buildCertificateRequest(TokenDTO token) throws Exception;

    X500Principal buildSertificateSubjetPrincipal();

    boolean checkSertificate(X509Certificate certificate);

    public void saveKeyPair(KeyPair keyPair) throws Exception;

    public X509Certificate getCertificateBySerialNumber(String serialNumber) throws Exception;

}
