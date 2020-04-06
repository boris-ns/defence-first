package rs.ac.uns.ftn.pkiservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.pkiservice.dto.response.SimpleCertificateDTO;
import rs.ac.uns.ftn.pkiservice.mapper.CertificateMapper;
import rs.ac.uns.ftn.pkiservice.service.CertificateService;

import java.io.IOException;
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
        System.out.println(certificate.getSubjectX500Principal().getName());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    // @TODO: treba da bude post i da mu se proslede informacije o subjektu
    @GetMapping(path = "/generate")
    public ResponseEntity<String> generate() throws CertificateException, UnrecoverableKeyException,
            NoSuchAlgorithmException,KeyStoreException, SignatureException, NoSuchProviderException,
            InvalidKeyException, IOException {

        // @TODO: Proslediti tip sertifikata servisnoj metodi
        X509Certificate certificate = certificateService.generateCertificate(null);
        System.out.println(certificate.getSubjectX500Principal().getName());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
