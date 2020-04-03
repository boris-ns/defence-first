package rs.ac.uns.ftn.pkiservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.security.cert.X509Certificate;
import java.util.Optional;

public interface CertificateRevocationListRepository extends MongoRepository<X509Certificate, String> {

    @Query(value = "{'revokedCertifications.certificationId' : ?0}", fields = "{'revokedCertifications.certificationId': 1, 'revokedCertifications.revocationDate': 1}")
    Optional<X509Certificate> findByCertificateId(String certificateId);
}
