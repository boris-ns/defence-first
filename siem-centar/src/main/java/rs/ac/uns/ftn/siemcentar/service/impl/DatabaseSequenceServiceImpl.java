package rs.ac.uns.ftn.siemcentar.service.impl;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemcentar.model.DatabaseSequence;

@Service
public class DatabaseSequenceServiceImpl extends DatabaseSequence {

    public long generateSequence(String seqName) {
        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
