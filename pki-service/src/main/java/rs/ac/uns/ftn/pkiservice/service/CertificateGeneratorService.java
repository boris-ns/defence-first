package rs.ac.uns.ftn.pkiservice.service;

import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;

import java.security.cert.X509Certificate;

public interface CertificateGeneratorService {

    X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData);
}
