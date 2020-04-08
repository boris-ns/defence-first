package rs.ac.uns.ftn.pkiservice.mapper;

import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import rs.ac.uns.ftn.pkiservice.dto.response.CertificateIssuerDTO;
import rs.ac.uns.ftn.pkiservice.dto.response.CertificateRequestDTO;
import rs.ac.uns.ftn.pkiservice.dto.response.SimpleCertificateDTO;

import javax.security.auth.x500.X500PrivateCredential;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;

public class CertificateMapper {

    public static SimpleCertificateDTO toSimpleCertificateDTO(X509Certificate certificate) {
        return new SimpleCertificateDTO(certificate.getSubjectX500Principal().toString(),
                certificate.getIssuerX500Principal().toString(), certificate.getSerialNumber(),
                certificate.getNotBefore(), certificate.getNotAfter());
    }

    public static CertificateRequestDTO toCertificateRequestDTO(X509Certificate certificate) {
        //Todo: dodati ekstenzije?
        return new CertificateRequestDTO(certificate.getSubjectX500Principal().toString(),
                null);
    }

    public static CertificateIssuerDTO toCertificateIssuerDTO(X500PrivateCredential credential) {
        return new CertificateIssuerDTO(credential.getCertificate().getSubjectX500Principal().toString(),
                credential.getAlias());
    }
}
