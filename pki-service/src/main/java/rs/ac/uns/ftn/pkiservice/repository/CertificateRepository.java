package rs.ac.uns.ftn.pkiservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.security.cert.X509Certificate;

@Repository
public interface CertificateRepository extends MongoRepository<X509Certificate, String> {
}
