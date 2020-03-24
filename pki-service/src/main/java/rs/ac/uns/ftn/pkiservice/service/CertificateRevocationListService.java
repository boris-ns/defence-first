package rs.ac.uns.ftn.pkiservice.service;

public interface CertificateRevocationListService {

    boolean checkIsRevoked(String id);
    void addCrlItem(String certificationId);
}
