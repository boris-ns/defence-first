package rs.ac.uns.ftn.siemcentar.service.impl;

import org.drools.core.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemcentar.model.Alarm;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.repository.AlarmRepository;
import rs.ac.uns.ftn.siemcentar.service.AlarmService;
import rs.ac.uns.ftn.siemcentar.service.DatabaseSequenceService;
import rs.ac.uns.ftn.siemcentar.service.KieSessionService;

import java.util.Collection;
import java.util.List;

@Service
public class AlarmServiceImpl implements AlarmService {

    private final KieSession kieSession;
    private final DatabaseSequenceService databaseSequenceService;
    private final AlarmRepository alarmRepository;

    @Autowired
    public AlarmServiceImpl(KieSessionService kieSessionService, DatabaseSequenceService databaseSequenceService,
                            AlarmRepository alarmRepository) {
        this.kieSession = kieSessionService.getKieSessionForAlarms();
        this.databaseSequenceService = databaseSequenceService;
        this.alarmRepository = alarmRepository;
    }

    @Override
    public List<Alarm> getAll() {
        return alarmRepository.findAll();
    }

    @Override
    public Alarm add(Alarm alarm) {
        return alarmRepository.save(alarm);
    }

    @Override
    public void processLogs(List<Log> logs) {
        for (Log log: logs) {
            kieSession.insert(log);
        }

        int ruleCount = kieSession.fireAllRules();
        System.out.println(ruleCount);

        Collection<Alarm> newAlarms = (Collection<Alarm>) kieSession.getObjects(new ClassObjectFilter(Alarm.class));
        for (Alarm alarm: newAlarms) {
            if(alarm.getId() == null)
                alarm.setId(databaseSequenceService.generateSequence(Alarm.SEQUENCE_NAME));
                add(alarm);
        }

    }
}
