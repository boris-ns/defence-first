package rs.ac.uns.ftn.pkiservice.controller;

import org.bouncycastle.cert.crmf.CRMFException;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.dto.response.CertificateIssuerDTO;
import rs.ac.uns.ftn.pkiservice.dto.response.CertificateRequestDTO;
import rs.ac.uns.ftn.pkiservice.dto.response.CreateCertificateDTO;
import rs.ac.uns.ftn.pkiservice.service.CertificateGeneratorService;
import rs.ac.uns.ftn.pkiservice.dto.response.SimpleCertificateDTO;
import rs.ac.uns.ftn.pkiservice.mapper.CertificateMapper;
import rs.ac.uns.ftn.pkiservice.service.CertificateService;
import rs.ac.uns.ftn.pkiservice.service.CertificateSigningRequestService;

import javax.security.auth.x500.X500PrivateCredential;
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
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<SimpleCertificateDTO>> findAll() {
        return new ResponseEntity<>(certificateService.findAllDto(), HttpStatus.OK);
    }

    @GetMapping(path = "/all/intermediate")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<CertificateIssuerDTO>> findAllRootAndIntermediate() {
        List<X500PrivateCredential> credentialList = certificateService.findAllRootAndIntermediate();
        List<CertificateIssuerDTO> certificateDTOS = credentialList.stream()
                .map(x -> CertificateMapper.toCertificateIssuerDTO(x))
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

    @PostMapping(path = "/generate/intermediate")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> generateIntermediate(@RequestBody CreateCertificateDTO certificateDTO) throws
            UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        X509Certificate certificate = certificateService.generateCertificateIntermediate(
                certificateDTO.getSubjectName(), certificateDTO.getIssuerAlias());
        return new ResponseEntity<>(certificate.getSerialNumber().toString(), HttpStatus.OK);
    }

    @PostMapping(path = "/generate")
    @PreAuthorize("hasRole('agent')")
    public ResponseEntity generate(@RequestBody String csr) throws IOException, OperatorCreationException, PKCSException {
        csrService.addRequest(csr);
        return ResponseEntity.ok().build();
    }
}
