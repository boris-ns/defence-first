package rs.ac.uns.ftn.siemcentar.service.impl;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemcentar.service.AlarmService;
import rs.ac.uns.ftn.siemcentar.service.KieSessionService;

@Service
public class KieSessionServiceImpl implements KieSessionService {

    @Value("${cep.kie.session.name}")
    public String cepKieSession;

    @Autowired
    private KieContainer kieContainer;

    @Autowired
    private AlarmService alarmService;

    @Override
    public KieSession getKieSessionForAlarms() {
        KieSession kieSession = kieContainer.newKieSession(cepKieSession);
//        kieSession.setGlobal( ALARM_SERVICE, alarmService);
        return kieSession;
    }
}
