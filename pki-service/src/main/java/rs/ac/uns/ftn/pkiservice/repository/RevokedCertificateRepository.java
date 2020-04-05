package rs.ac.uns.ftn.pkiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.pkiservice.models.RevokedCertificate;

import java.util.Optional;

public interface RevokedCertificateRepository extends JpaRepository<RevokedCertificate, Long> {

    Optional<RevokedCertificate> findBySerialNumber(String serialNumber);
}
