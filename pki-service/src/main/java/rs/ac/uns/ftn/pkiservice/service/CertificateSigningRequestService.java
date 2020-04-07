package rs.ac.uns.ftn.pkiservice.service;

import org.bouncycastle.cert.crmf.CRMFException;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCSException;
import rs.ac.uns.ftn.pkiservice.models.CertificateSigningRequest;
import rs.ac.uns.ftn.pkiservice.models.enums.CSRStatus;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

public interface CertificateSigningRequestService {

    List<CertificateSigningRequest> findAllWaitingRequests();
    PKCS10CertificationRequest isValidSigned(String pemString) throws PKCSException, IOException, OperatorCreationException;
    void addRequest(String csr) throws PKCSException, IOException, OperatorCreationException;
    void changeStatus(Long id, CSRStatus toStatus) throws Exception;
    X509Certificate parseCertificateRequest(String csr) throws Exception;
}
