package rs.ac.uns.ftn.siemcentar.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.lang3.SerializationUtils;
import org.kie.api.runtime.KieSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemcentar.dto.request.LogFilterDTO;
import rs.ac.uns.ftn.siemcentar.exception.exceptions.ApiRequestException;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.model.LogType;
import rs.ac.uns.ftn.siemcentar.repository.LogRepository;
import rs.ac.uns.ftn.siemcentar.service.AlarmService;
import rs.ac.uns.ftn.siemcentar.service.LogService;

import rs.ac.uns.ftn.siemcentar.utils.DateUtils;

import java.io.IOException;
import java.security.PublicKey;
import java.security.Signature;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private AlarmService alarmService;

    @Override
    public void saveLogs(List<Log> logs){
        logRepository.saveAll(logs);
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


    private void verifyLogSignature(Log l, String logStr, PublicKey publicKey) throws Exception {

        String recivedSignature = l.getSignature();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(logStr.getBytes());

        byte[] recivedSignatureBytes = Base64.getDecoder().decode(recivedSignature);

        boolean match = signature.verify(recivedSignatureBytes);

        if(!match){
            String message = "Invalid log signature for log: " + l.toString();
            throw new ApiRequestException(message);
        }
    }

    @Override
    public void verifyLogsSigns(ArrayList<Log> logs, PublicKey publicKey) throws Exception{
        ArrayList<String> logStrs = convertToStrArray(logs);
        for(int i=0; i < logs.size(); i++) {
            verifyLogSignature(logs.get(i), logStrs.get(i) ,publicKey);
        }
    }

    private ArrayList<String> convertToStrArray(ArrayList<Log> logs) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ArrayList<String> stringLogs = new ArrayList<>();
        for( Log l : logs) {
            String recivedSign = l.getSignature();
            l.setSignature("");
            stringLogs.add(ow.writeValueAsString(l));
            l.setSignature(recivedSign);
        }
        return stringLogs;
    }



}
