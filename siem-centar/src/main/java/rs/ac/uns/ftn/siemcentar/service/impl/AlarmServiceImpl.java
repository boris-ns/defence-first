package rs.ac.uns.ftn.siemcentar.service.impl;

import org.drools.core.time.SessionPseudoClock;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemcentar.constants.Constants;
import rs.ac.uns.ftn.siemcentar.model.Alarm;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.repository.AlarmRepository;
import rs.ac.uns.ftn.siemcentar.service.AlarmService;
import rs.ac.uns.ftn.siemcentar.service.KieSessionService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private KieSessionService kieSessionService;

    @Autowired
    private AlarmRepository alarmRepository;

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

        KieSession kieSession = kieSessionService.getKieSessionForAlarms();
        SessionPseudoClock clock = kieSession.getSessionClock();

        System.out.println("pocetno vreme sata: " + clock.getCurrentTime());

            //@TODO: dodati kasnije
//        kieSession.setGlobal(Constants.ALARM_SERVICE, this);
        for (int index = 0; index < logs.size(); index++) {

            kieSession.insert(logs.get(index));
            clock.advanceTime(1, TimeUnit.SECONDS);

            int ruleCount = kieSession.fireAllRules();
            System.out.println("index: " + index + " broj pravila " + ruleCount );


            ObjectFilter payPassFilter = new ObjectFilter() {
                @Override
                public boolean accept(Object object) {
                    if ( Alarm.class.equals(object.getClass())) return true;
                    if ( Alarm.class.equals(object.getClass().getSuperclass())) return true;
                    return false;
                }
            };

            List<Alarm> facts = new ArrayList<>();
            for (FactHandle handle : kieSession.getFactHandles(payPassFilter)) {
                facts.add((Alarm) kieSession.getObject(handle));
            }

            System.out.println("svi Alarmi koji su trenutno u sesiji");
            System.out.println(facts);
        }
    }
}
