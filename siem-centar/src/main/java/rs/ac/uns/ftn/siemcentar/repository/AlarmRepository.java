package rs.ac.uns.ftn.siemcentar.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.siemcentar.model.Alarm;

import java.util.Date;

@Repository
public interface AlarmRepository extends MongoRepository<Alarm, Long> {

    @Query(value = "{'agent': ?0}", count = true)
    Long countByAgentName(String agentName);

    @Query(value = "{'agent': ?0, 'date': { $gte: ?1, $lte: ?2 }}", count = true)
    Long countByAgentNameAndDates(String agentName, Date start, Date end);

    @Query(value = "{'source': ?0, 'agent': ?1}", count = true)
    Long countBySourceAndAgent(String source, String agent);

    @Query(value = "{'source': ?0, 'agent': ?1, 'date': { $gte: ?2, $lte: ?3 }}", count = true)
    Long countBySourceAndAgentAndDates(String source, String agent, Date start, Date end);
}
