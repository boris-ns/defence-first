package rs.ac.uns.ftn.siemcentar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.siemcentar.constants.IpBlacklist;
import rs.ac.uns.ftn.siemcentar.dto.request.LogFilterDTO;
import rs.ac.uns.ftn.siemcentar.dto.response.LogDTO;
import rs.ac.uns.ftn.siemcentar.mapper.LogMapper;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.service.CertificateService;
import rs.ac.uns.ftn.siemcentar.service.DatabaseSequenceService;
import rs.ac.uns.ftn.siemcentar.service.LogService;
import java.util.*;

@RestController
@RequestMapping(value = "/api/log")
public class LogController {

    @Autowired
    private LogService logService;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private DatabaseSequenceService databaseSequenceService;

    @PostMapping(path = "/send")
    @PreAuthorize("hasRole('agent')")
    public  ResponseEntity<String> preMasterSecret(@RequestBody ArrayList<String> logs) throws Exception{
        List<Log> logList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for(String l: logs) {
            System.out.println(l);
            Log log = mapper.readValue(l, Log.class);
            log.setId(databaseSequenceService.generateSequence(Log.SEQUENCE_NAME));

            if (IpBlacklist.BLACKLIST.contains(log.getIp())) {
                log.setIpBlacklisted(true);
            } else {
                log.setIpBlacklisted(false);
            }

            logList.add(log);
        }

        this.logService.saveLogs(logList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/findAll")
    @PreAuthorize("hasRole('admin') or hasRole('operator')")
    public ResponseEntity<List<LogDTO>> findAll() {
        List<Log> logs = logService.findAll();
        return new ResponseEntity<>(LogMapper.toListDto(logs), HttpStatus.OK);
    }

    @PostMapping(path = "/filter")
    @PreAuthorize("hasRole('admin') or hasRole('operator')")
    public ResponseEntity<List<LogDTO>> filter(@RequestBody LogFilterDTO filter) {
        List<Log> logs = logService.searchAndFilter(filter);
        return new ResponseEntity<>(LogMapper.toListDto(logs), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasRole('agent')")
    public ResponseEntity<Void> saveLogs(@RequestBody List<Log> logs) {
        this.logService.saveLogs(logs);
        return new ResponseEntity(HttpStatus.OK);
    }

}
