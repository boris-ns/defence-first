package rs.ac.uns.ftn.pkiservice.controller;

import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.crmf.CRMFException;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;
import rs.ac.uns.ftn.pkiservice.service.CertificateGeneratorService;
import rs.ac.uns.ftn.pkiservice.service.CertificateService;

import java.io.IOException;
import java.io.StringWriter;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@RestController
@RequestMapping(value = "/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CertificateGeneratorService certificateGeneratorService;


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

    // @TODO: da ga doda u lanac, za sad ga samo cuva ovako
    @PostMapping(path = "/generate")
    public ResponseEntity<String> generate(@RequestBody String csr) throws CertificateException, UnrecoverableKeyException,
            NoSuchAlgorithmException, KeyStoreException, SignatureException, NoSuchProviderException,
            InvalidKeyException, IOException, OperatorCreationException, PKCSException, CryptoException, CRMFException {
        
        X509Certificate newCert = certificateGeneratorService.parseCertificateRequest(csr);
        certificateService.writeCertificateToKeyStore(newCert, Constants.CERT_TYPE.LEAF_CERT, null);

        StringWriter sw = new StringWriter();
        JcaPEMWriter pm = new JcaPEMWriter(sw);
        pm.writeObject(newCert);
        pm.close();
        return new ResponseEntity<>(sw.toString(), HttpStatus.OK);
    }
}
