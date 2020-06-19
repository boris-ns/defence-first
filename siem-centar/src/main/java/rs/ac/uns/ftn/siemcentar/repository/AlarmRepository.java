package rs.ac.uns.ftn.siemcentar.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.siemcentar.model.Alarm;

@Repository
public interface AlarmRepository extends MongoRepository<Alarm, Long> {

    @Query(value = "{'agent': ?0}", count = true)
    Long countByAgentName(String agentName);

    @Query(value = "{'source': ?0, 'agent': ?1}", count = true)
    Long countBySourceAndAgent(String source, String agent);
}
