package rs.ac.uns.ftn.siemcentar.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.siemcentar.model.Log;

@Repository
public interface LogRepository extends MongoRepository<Log, Long> {

    // @NOTE: Ovaj upit bi trebalo da radi normalno, ali problem nastaje kada se ne prosledi neki parametar
    // a u mongu nema 'is null' operatora kao u SQL-u. Tako da ovaj upit zahteva da mu se proslede svi
    // parametri da bi ispravno radio
//    @Query("{'source': {$regex: ?0}, 'logType': ?1, 'message': {$regex: ?2}, 'id': ?3, 'date': {$gte: ?4}, 'date': {$lte: ?5}}")
//    List<Log> filter(String source, String logType, String message, Long id, Date startDate, Date endDate);

}
