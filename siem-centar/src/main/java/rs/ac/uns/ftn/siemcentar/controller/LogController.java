package rs.ac.uns.ftn.siemcentar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.model.LogType;
import rs.ac.uns.ftn.siemcentar.service.LogService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/log")
public class LogController {


    @Autowired
    private LogService logService;

    @GetMapping(path = "/findAll")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<Log>> findAll() {
        return new ResponseEntity<>(logService.findAll(), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasRole('agent')")
    public ResponseEntity<Void> saveLogs(@RequestBody List<Log> logs) {
        this.logService.saveLogs(logs);
        return new ResponseEntity(HttpStatus.OK);
    }

}
