package rs.ac.uns.ftn.pkiservice.service;

import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;

import javax.security.auth.x500.X500PrivateCredential;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CertificateService {

    Map<Constants.CERT_TYPE, List<X509Certificate>> findAll();

    Map<String, Boolean> findAllRevoked();

    List<X500PrivateCredential> findAllRootAndIntermediate();

    X509Certificate findCertificateByAlias(String alias);

    IssuerData findIssuerByAlias(String alias) throws NoSuchAlgorithmException, CertificateException,
            KeyStoreException, IOException, UnrecoverableKeyException;

    X509Certificate generateCertificateIntermediate(HashMap<String, String> subjectName, String issuerAlias) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException;

    Certificate[] getCertificateChainByAlias(String alias);

    Certificate[] createChain(String issuerAlias, Certificate certificate);

    void writeCertificateToKeyStore(String alias, Certificate[] certificates, PrivateKey pk)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException;

    void replace(String alias) throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException,
            KeyStoreException, IOException;


    void writeCertToFile(String serialNumber, String certDirectoryPath) throws Exception;

}
