package rs.ac.uns.ftn.pkiservice.service;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCSException;
import rs.ac.uns.ftn.pkiservice.models.CertificateSigningRequest;
import rs.ac.uns.ftn.pkiservice.models.enums.CSRStatus;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.List;

public interface CertificateSigningRequestService {

    List<CertificateSigningRequest> findAllWaitingRequests();
    PKCS10CertificationRequest isValidSigned(String pemString, Boolean renew) throws PKCSException, IOException, OperatorCreationException;
    void addRequest(String csr, String username) throws PKCSException, IOException, OperatorCreationException;
    void addRenewRequest(String csr, String username) throws PKCSException, IOException, OperatorCreationException;
    void changeStatus(Long id, CSRStatus toStatus) throws Exception;
    X509Certificate saveCertificateRequest(String csr, Boolean renewal) throws Exception;
    CertificateSigningRequest getCertsRequest(String name);
}
