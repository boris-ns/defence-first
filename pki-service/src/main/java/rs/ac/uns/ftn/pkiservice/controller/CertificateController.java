package rs.ac.uns.ftn.pkiservice.controller;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.dto.response.CertificateIssuerDTO;
import rs.ac.uns.ftn.pkiservice.dto.request.CreateCertificateDTO;
import rs.ac.uns.ftn.pkiservice.dto.response.SimpleCertificateDTO;
import rs.ac.uns.ftn.pkiservice.mapper.CertificateMapper;
import rs.ac.uns.ftn.pkiservice.mapper.NameMapper;
import rs.ac.uns.ftn.pkiservice.service.CertificateService;

import javax.security.auth.x500.X500PrivateCredential;
import javax.validation.Valid;
import java.io.IOException;
import java.io.StringWriter;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @GetMapping(path = "/all")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<SimpleCertificateDTO>> findAll() {
        Map<Constants.CERT_TYPE, List<X509Certificate>> certifictes = certificateService.findAll();
        Map<String, Boolean> revoked = certificateService.findAllRevoked();
        List<SimpleCertificateDTO> result = new ArrayList<>();
        for (Constants.CERT_TYPE key : certifictes.keySet()) {
            for (X509Certificate cert : certifictes.get(key)) {

                System.out.println(cert.getSerialNumber());
                if(revoked.containsKey(cert.getSerialNumber())) {
                    result.add(new SimpleCertificateDTO(cert, revoked.get(cert.getSerialNumber().toString()), key));
                }
                else {
                    result.add(new SimpleCertificateDTO(cert, false, key));
                }


            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
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

    @GetMapping(path = "/alias/{alias}")
    public ResponseEntity<String> findByAlias(@PathVariable String alias) throws IOException {
        X509Certificate certificate = certificateService.findCertificateByAlias(alias);
        StringWriter sw = new StringWriter();
        JcaPEMWriter pm = new JcaPEMWriter(sw);
        pm.writeObject(certificate);
        pm.close();
        return new ResponseEntity<>(sw.toString(), HttpStatus.OK);
    }

    @PostMapping(path = "/generate/intermediate", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity generateIntermediate(@Valid @RequestBody CreateCertificateDTO certificateDTO) throws
            Exception {
        X500Name subjectName = NameMapper.toX500Name(certificateDTO);
        certificateService.generateCertificateIntermediate(subjectName, certificateDTO.getIssuerAlias());
        return ResponseEntity.ok().build();
    }


    @PutMapping(path = "/replace/{alias}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity replace(@PathVariable String alias) throws
            UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        certificateService.replace(alias);
        return ResponseEntity.ok().build();
    }
}
