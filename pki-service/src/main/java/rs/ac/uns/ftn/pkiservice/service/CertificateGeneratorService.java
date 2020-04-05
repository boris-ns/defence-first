package rs.ac.uns.ftn.pkiservice.service;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public interface CertificateGeneratorService {

    X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, Constants.CERT_TYPE typeCert);

    IssuerData generateIssuerData(PrivateKey issuerKey, X500Name name);

    SubjectData generateSubjectData(PublicKey publicKey, X500Name name, Constants.CERT_TYPE certType);


}
