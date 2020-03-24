package rs.ac.uns.ftn.pkiservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.pkiservice.service.CertificateRevocationListService;

@RestController
@RequestMapping(value = "/api/crl")
public class CertificateRevocationListController {

    @Autowired
    private CertificateRevocationListService crlService;

    @GetMapping("/{id}")
    public ResponseEntity<Boolean> checkIsRevoked(@PathVariable String id) {
        return new ResponseEntity<>(crlService.checkIsRevoked(id), HttpStatus.OK);
    }

    @PostMapping("/{certificationId}")
    public ResponseEntity addItemToCrl(@PathVariable String certificationId) {
        crlService.addCrlItem(certificationId);
        return ResponseEntity.ok().build();
    }
}
