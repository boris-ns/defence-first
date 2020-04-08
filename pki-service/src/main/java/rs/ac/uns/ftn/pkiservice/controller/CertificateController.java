package rs.ac.uns.ftn.pkiservice.controller;

import org.bouncycastle.cert.crmf.CRMFException;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.pkiservice.dto.response.CertificateRequestDTO;
import rs.ac.uns.ftn.pkiservice.dto.response.CreateCertificateDTO;
import rs.ac.uns.ftn.pkiservice.service.CertificateGeneratorService;
import rs.ac.uns.ftn.pkiservice.dto.response.SimpleCertificateDTO;
import rs.ac.uns.ftn.pkiservice.mapper.CertificateMapper;
import rs.ac.uns.ftn.pkiservice.service.CertificateService;
import rs.ac.uns.ftn.pkiservice.service.CertificateSigningRequestService;

import java.io.IOException;
import java.io.StringWriter;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CertificateGeneratorService certificateGeneratorService;

    @Autowired
    private CertificateSigningRequestService csrService;


    @GetMapping(path = "/all")
    public ResponseEntity<List<SimpleCertificateDTO>> findAll() {
        List<X509Certificate> certificateList = certificateService.findAll();
        List<SimpleCertificateDTO> certificateDTOS = certificateList.stream()
                .map(x -> CertificateMapper.toSimpleCertificateDTO(x))
                .collect(Collectors.toList());
        return new ResponseEntity<>(certificateDTOS, HttpStatus.OK);
    }
    
    // @TODO: CHANGE LATER!!!!!!
    // Return DTO, not the object from database
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> findById(@PathVariable String id, Principal a) {
        System.out.println(a.getName());
//        Certificate certificate = certificateService.findById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping(path = "/alias/{alias}")
    public ResponseEntity<String> findByAlias(@PathVariable String alias) throws CertificateException,
            UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException,
            IOException {

        X509Certificate certificate = certificateService.findCertificateByAlias(alias);
        StringWriter sw = new StringWriter();
        JcaPEMWriter pm = new JcaPEMWriter(sw);
        pm.writeObject(certificate);
        pm.close();
        return new ResponseEntity<>(sw.toString(), HttpStatus.OK);
    }

    @GetMapping(path = "/requests")
    public ResponseEntity<List<CertificateRequestDTO>> findAllRequests() {
        //Todo: zameni ti kasnije, kad se implementiram metoda findAllRequests
//        List<X509Certificate> certificateList = certificateService.findAllRequests();
        List<X509Certificate> certificateList = certificateService.findAll();
        List<CertificateRequestDTO> certificateDTOS = certificateList.stream()
                .map(x -> CertificateMapper.toCertificateRequestDTO(x))
                .collect(Collectors.toList());
        return new ResponseEntity<>(certificateDTOS, HttpStatus.OK);
    }

    @PostMapping(path = "/generate/intermediate")
    public ResponseEntity<String> generateIntermediate(@RequestBody CreateCertificateDTO certificateDTO) throws
            UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        certificateService.generateCertificateIntermediate("", certificateDTO.getIssuerAlias());

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping(path = "/generate")
    public ResponseEntity generate(@RequestBody String csr) throws IOException, OperatorCreationException, PKCSException {
        csrService.addRequest(csr);
        return ResponseEntity.ok().build();
    }
}
