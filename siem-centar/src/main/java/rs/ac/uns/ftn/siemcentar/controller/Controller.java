package rs.ac.uns.ftn.siemcentar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.siemcentar.service.CertificateService;

@RestController
@RequestMapping(value = "/api")
public class Controller {



    @GetMapping(path = "/helloworld")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> helloWorld() {

        return new ResponseEntity<>("Hello world", HttpStatus.OK);
    }
}
