package rs.ac.uns.ftn.siemcentar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.siemcentar.dto.request.ReportRequestDTO;
import rs.ac.uns.ftn.siemcentar.dto.response.ReportsDTO;
import rs.ac.uns.ftn.siemcentar.service.ReportsService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/reports")
public class ReportsController {

    @Autowired
    private ReportsService reportsService;

    @PostMapping
    @PreAuthorize("hasRole('admin') or hasRole('operator')")
    public ResponseEntity<ReportsDTO> getReports(@Valid @RequestBody ReportRequestDTO requestDto) {
        return new ResponseEntity<>(reportsService.createReports(requestDto), HttpStatus.OK);
    }
}
