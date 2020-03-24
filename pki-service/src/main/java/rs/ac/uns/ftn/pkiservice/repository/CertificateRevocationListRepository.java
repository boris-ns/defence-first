package rs.ac.uns.ftn.pkiservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import rs.ac.uns.ftn.pkiservice.models.CertificateRevocationItem;
import rs.ac.uns.ftn.pkiservice.models.CertificateRevocationList;

import java.util.Optional;

public interface CertificateRevocationListRepository extends MongoRepository<CertificateRevocationList, String> {

    @Query(value = "{'revokedCertifications.certificationId' : ?0}", fields = "{'revokedCertifications.certificationId': 1, 'revokedCertifications.revocationDate': 1}")
    Optional<CertificateRevocationItem> findByCertificateId(String certificateId);
}
