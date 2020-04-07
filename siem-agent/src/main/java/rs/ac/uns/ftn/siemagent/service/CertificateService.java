package rs.ac.uns.ftn.siemagent.service;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;

public interface CertificateService {

    String buildCertificateRequest() throws IOException;

    X500Principal buildSertificateSubjetPrincipal();
}
