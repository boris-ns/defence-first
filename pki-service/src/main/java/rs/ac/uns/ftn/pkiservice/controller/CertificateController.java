package rs.ac.uns.ftn.pkiservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.pkiservice.models.Certificate;
import rs.ac.uns.ftn.pkiservice.service.CertificateService;

@RestController
@RequestMapping(value = "/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    // @TODO: CHANGE LATER!!!!!!
    // Return DTO, not the object from database
    @GetMapping(path = "/{id}")
    public ResponseEntity<Certificate> findById(@PathVariable String id) {
        Certificate certificate = certificateService.findById(id);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }
}
