package rs.ac.uns.ftn.pkiservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.pkiservice.service.OCSPService;

@RestController
@RequestMapping(value = "/api/ocsp")
public class OCSPController {
    
    @Autowired
    private OCSPService ocspService;

    @PostMapping(value = "/{serialNumber}")
    public ResponseEntity addCertificateToOCSP(@PathVariable String serialNumber) {
        ocspService.addCertificateToOCSP(serialNumber);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{serialNumber}")
    public ResponseEntity<Boolean> checkIsCertificateRevoked(@PathVariable String serialNumber) {
        return new ResponseEntity<>(ocspService.isCertificateRevoked(serialNumber), HttpStatus.OK);
    }
}
