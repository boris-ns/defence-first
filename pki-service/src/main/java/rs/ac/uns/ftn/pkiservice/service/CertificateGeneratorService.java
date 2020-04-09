package rs.ac.uns.ftn.pkiservice.service;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.crmf.CRMFException;
import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;

import java.security.*;
import java.security.cert.X509Certificate;
import java.util.HashMap;

public interface CertificateGeneratorService {

    X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, Constants.CERT_TYPE typeCert);

    IssuerData generateIssuerData(PrivateKey issuerKey, X500Name name);

    SubjectData generateSubjectData(PublicKey publicKey, X500Name name, Constants.CERT_TYPE certType);

    PublicKey toPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) throws CRMFException;

    void generateDate(SubjectData subjectData, Constants.CERT_TYPE certType);

    X500Name generateName(HashMap<String, String> subjectName);
}
