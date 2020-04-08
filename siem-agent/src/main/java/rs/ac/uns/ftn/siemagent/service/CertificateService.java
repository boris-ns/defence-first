package rs.ac.uns.ftn.siemagent.service;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;

public interface CertificateService {

    String buildCertificateRequest() throws Exception;

    X500Principal buildSertificateSubjetPrincipal();

    boolean checkSertificate(X509Certificate certificate);

    public void saveKeyPair(KeyPair keyPair) throws Exception;

    public X509Certificate getCertificateBySerialNumber(String serialNumber) throws Exception;

}
