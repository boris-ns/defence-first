package rs.ac.uns.ftn.siemcentar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.service.CertificateService;
import rs.ac.uns.ftn.siemcentar.service.LogService;
import java.util.*;

@RestController
@RequestMapping(value = "/api/log")
public class LogController {

    @Autowired
    private LogService logService;

    @Autowired
    private CertificateService certificateService;

    @PostMapping(path = "/send")
    @PreAuthorize("hasRole('agent')")
    public  ResponseEntity<String> preMasterSecret(@RequestBody ArrayList<String> logs) throws Exception{
        List<Log> logList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for(String l: logs) {
            System.out.println(l);
            logList.add(mapper.readValue(l, Log.class));
        }

        this.logService.saveLogs(logList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/findAll")
    @PreAuthorize("hasRole('admin') or hasRole('operator')")
    public ResponseEntity<List<Log>> findAll() {
        return new ResponseEntity<>(logService.findAll(), HttpStatus.OK);
    }

    
}
