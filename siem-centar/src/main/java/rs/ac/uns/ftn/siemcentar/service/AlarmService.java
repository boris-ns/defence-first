package rs.ac.uns.ftn.siemcentar.service;

import rs.ac.uns.ftn.siemcentar.model.Alarm;

import java.util.List;

public interface AlarmService {

    List<Alarm> getAll();
    Alarm add(Alarm alarm);
}
