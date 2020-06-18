package rs.ac.uns.ftn.siemcentar.service.impl;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemcentar.constants.Constants;
import rs.ac.uns.ftn.siemcentar.messaging.WebSocketProducer;
import rs.ac.uns.ftn.siemcentar.dto.request.LogFilterDTO;
import rs.ac.uns.ftn.siemcentar.exception.exceptions.ApiRequestException;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.model.LogType;
import rs.ac.uns.ftn.siemcentar.repository.LogRepository;
import rs.ac.uns.ftn.siemcentar.service.AlarmService;
import rs.ac.uns.ftn.siemcentar.service.KieSessionService;
import rs.ac.uns.ftn.siemcentar.service.LogService;

import rs.ac.uns.ftn.siemcentar.utils.DateUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private WebSocketProducer socketProducer;

    @Autowired
    private AlarmService alarmService;

    @Override
    public void saveLogs(List<Log> logs) {
        logRepository.saveAll(logs);
        logs.forEach(log -> socketProducer.sendLog(log));

        alarmService.processLogs(logs);
    }

    @Override
    public List<Log> findAll() {
        return logRepository.findAll();
    }

    @Override
    public List<Log> searchAndFilter(LogFilterDTO filter) {
        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            if (DateUtils.dateFrom(filter.getStartDate()).after(DateUtils.dateFrom(filter.getEndDate()))) {
                throw new ApiRequestException("Start date must be before end date.");
            }
        }

        List<Log> logs = logRepository.findAll();

        if (filter.getId() != null)
            logs = logs.stream()
                    .filter(log -> log.getId().equals(filter.getId())).collect(Collectors.toList());

        if (filter.getLogType() != null)
            logs = logs.stream()
                    .filter(log -> log.getLogType().equals(LogType.valueOf(filter.getLogType()))).collect(Collectors.toList());

        if (filter.getMessage() != null)
            logs = logs.stream()
                    .filter(log -> log.getMessage().matches(filter.getMessage())).collect(Collectors.toList());

        if (filter.getSource() != null)
            logs = logs.stream()
                    .filter(log -> log.getSource().matches(filter.getSource())).collect(Collectors.toList());

        if (filter.getStartDate() != null) {
            Date startDate = DateUtils.dateFrom(filter.getStartDate());
            logs = logs.stream()
                    .filter(log -> log.getDate().after(startDate)).collect(Collectors.toList());
        }

        if (filter.getEndDate() != null) {
            Date endDate = DateUtils.dateFrom(filter.getEndDate());
            logs = logs.stream()
                    .filter(log -> log.getDate().before(endDate)).collect(Collectors.toList());
        }

        return logs;
    }

}
