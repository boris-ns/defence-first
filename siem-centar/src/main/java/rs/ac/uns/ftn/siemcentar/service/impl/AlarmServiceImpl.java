package rs.ac.uns.ftn.siemcentar.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemcentar.model.Alarm;
import rs.ac.uns.ftn.siemcentar.repository.AlarmRepository;
import rs.ac.uns.ftn.siemcentar.service.AlarmService;

import java.util.List;

@Service
public class AlarmServiceImpl implements AlarmService {

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
}
