package rs.ac.uns.ftn.pkiservice.service;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;
import rs.ac.uns.ftn.pkiservice.models.CertificateSigningRequest;
import rs.ac.uns.ftn.pkiservice.models.enums.CSRStatus;

import java.io.IOException;
import java.util.List;

public interface CertificateSigningRequestService {

    List<CertificateSigningRequest> findAllWaitingRequests();
    void addRequest(String csr) throws PKCSException, IOException, OperatorCreationException;
    void changeStatus(Long id, CSRStatus toStatus);
}
