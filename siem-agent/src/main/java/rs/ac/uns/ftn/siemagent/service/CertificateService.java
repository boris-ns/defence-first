package rs.ac.uns.ftn.siemagent.service;

import rs.ac.uns.ftn.siemagent.dto.response.TokenDTO;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;

public interface CertificateService {

    String buildCertificateRequest(TokenDTO token) throws IOException;

    X500Principal buildSertificateSubjetPrincipal();
}
