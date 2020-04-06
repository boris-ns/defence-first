package rs.ac.uns.ftn.pkiservice.mapper;

import rs.ac.uns.ftn.pkiservice.dto.response.SimpleCertificateDTO;

import java.security.cert.X509Certificate;

public class CertificateMapper {

    public static SimpleCertificateDTO toSimpleCertificateDTO(X509Certificate certificate) {
        return new SimpleCertificateDTO(certificate.getSubjectX500Principal().toString(),
                certificate.getIssuerX500Principal().toString(), certificate.getSerialNumber(),
                certificate.getNotBefore(), certificate.getNotAfter());
    }
}
