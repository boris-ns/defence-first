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

    private final KieContainer kieContainer;

    @Autowired
    public KieSessionServiceImpl(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    @Override
    public KieSession getKieSessionForAlarms() {
        return kieContainer.newKieSession(cepKieSession);
    }
}
