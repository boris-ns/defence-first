package rs.ac.uns.ftn.pkiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.pkiservice.models.CertificateSigningRequest;
import rs.ac.uns.ftn.pkiservice.models.enums.CSRStatus;

import java.util.List;

@Repository
public interface CertificateSigningRequestRepository extends JpaRepository<CertificateSigningRequest, Long> {

    List<CertificateSigningRequest> findByStatus(CSRStatus status);

    CertificateSigningRequest findByUsername(String name);
}
