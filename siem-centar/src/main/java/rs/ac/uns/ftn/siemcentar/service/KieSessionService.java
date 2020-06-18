package rs.ac.uns.ftn.siemcentar.service;

import org.kie.api.runtime.KieSession;

public interface KieSessionService {

    KieSession getKieSessionForAlarms();
}
