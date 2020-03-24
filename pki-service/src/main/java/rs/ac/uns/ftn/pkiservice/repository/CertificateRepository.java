package rs.ac.uns.ftn.pkiservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.pkiservice.models.Certificate;

@Repository
public interface CertificateRepository extends MongoRepository<Certificate, String> {
}
