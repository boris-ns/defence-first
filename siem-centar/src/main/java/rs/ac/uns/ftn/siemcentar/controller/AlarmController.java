package rs.ac.uns.ftn.siemcentar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.siemcentar.dto.response.AlarmDTO;
import rs.ac.uns.ftn.siemcentar.mapper.AlarmMapper;
import rs.ac.uns.ftn.siemcentar.model.Alarm;
import rs.ac.uns.ftn.siemcentar.service.AlarmService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/alarm")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @GetMapping(path = "/all")
    @PreAuthorize("hasRole('admin') or hasRole('operator')")
    public ResponseEntity<List<AlarmDTO>> findAll() {
        List<Alarm> alarms = alarmService.getAll();
        return new ResponseEntity<>(AlarmMapper.toListDto(alarms), HttpStatus.OK);
    }
}
