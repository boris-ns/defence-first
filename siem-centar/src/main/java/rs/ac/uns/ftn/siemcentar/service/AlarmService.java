package rs.ac.uns.ftn.siemcentar.service;

import rs.ac.uns.ftn.siemcentar.model.Alarm;
import rs.ac.uns.ftn.siemcentar.model.Log;

import java.util.List;

public interface AlarmService {

    List<Alarm> getAll();
    Alarm add(Alarm alarm);

    void processLogs(List<Log> logs);

}
