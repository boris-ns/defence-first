package rs.ac.uns.ftn.siemcentar.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.siemcentar.model.Alarm;

@Repository
public interface AlarmRepository extends MongoRepository<Alarm, Long> {
}
