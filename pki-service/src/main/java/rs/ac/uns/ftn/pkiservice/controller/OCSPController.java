package rs.ac.uns.ftn.pkiservice.controller;

import org.bouncycastle.cert.ocsp.*;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.pkiservice.service.OCSPService;

import java.io.IOException;
import java.io.StringWriter;

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

    @PostMapping(value = "/check")
    public ResponseEntity<byte[]> checkIsCertificateRevoked(@RequestBody byte[] request) throws Exception{
        OCSPReq ocspReq = new OCSPReq(request);
        OCSPResp ocspResp = ocspService.generateOCSPResponse(ocspReq);
        byte[] response = ocspResp.getEncoded();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
