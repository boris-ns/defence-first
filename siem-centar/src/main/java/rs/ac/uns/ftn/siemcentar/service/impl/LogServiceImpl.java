package rs.ac.uns.ftn.siemcentar.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.repository.LogRepository;
import rs.ac.uns.ftn.siemcentar.service.LogService;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public void saveLogs(List<Log> logs) {
        logRepository.saveAll(logs);
    }

    @Override
    public List<Log> findAll() {
        return logRepository.findAll();
    }


}
