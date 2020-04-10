package rs.ac.uns.ftn.pkiservice.controller;

import org.bouncycastle.cert.ocsp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.pkiservice.service.OCSPService;

@RestController
@RequestMapping(value = "/api/ocsp")
public class OCSPController {
    
    @Autowired
    private OCSPService ocspService;

    @PostMapping(value = "/{serialNumber}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity addCertificateToOCSP(@PathVariable String serialNumber) {
        ocspService.addCertificateToOCSP(serialNumber);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/check")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<byte[]> checkIsCertificateRevoked(@RequestBody byte[] request) throws Exception{
        OCSPReq ocspReq = new OCSPReq(request);
        OCSPResp ocspResp = ocspService.generateOCSPResponse(ocspReq);
        byte[] response = ocspResp.getEncoded();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
