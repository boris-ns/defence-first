package rs.ac.uns.ftn.pkiservice.service;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.crmf.CRMFException;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCSException;
import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

public interface CertificateGeneratorService {

    X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, Constants.CERT_TYPE typeCert);

    IssuerData generateIssuerData(PrivateKey issuerKey, X500Name name);

    SubjectData generateSubjectData(PublicKey publicKey, X500Name name, Constants.CERT_TYPE certType);

    X509Certificate parseCertificateRequest(String csr) throws IOException, OperatorCreationException, PKCSException, CryptoException, CRMFException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException;

    PublicKey toPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) throws CRMFException;

    X500Name generateName(HashMap<String, String> subjectName);
}
