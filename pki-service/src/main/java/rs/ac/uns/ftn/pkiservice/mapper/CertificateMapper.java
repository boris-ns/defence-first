package rs.ac.uns.ftn.pkiservice.mapper;

import rs.ac.uns.ftn.pkiservice.dto.response.CertificateIssuerDTO;

import javax.security.auth.x500.X500PrivateCredential;

public class CertificateMapper {

    public static CertificateIssuerDTO toCertificateIssuerDTO(X500PrivateCredential credential) {
        return new CertificateIssuerDTO(credential.getCertificate().getSubjectX500Principal().toString(),
                credential.getAlias());
    }
}
