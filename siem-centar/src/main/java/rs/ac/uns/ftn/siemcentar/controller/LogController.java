package rs.ac.uns.ftn.siemcentar.controller;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.model.LogType;
import rs.ac.uns.ftn.siemcentar.service.CertificateService;
import rs.ac.uns.ftn.siemcentar.service.CipherService;
import rs.ac.uns.ftn.siemcentar.service.LogService;
import rs.ac.uns.ftn.siemcentar.service.OCSPService;

import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/log")
public class LogController {


    @Autowired
    private LogService logService;

    @Autowired
    private CipherService cipherService;

    @Autowired
    private CertificateService certificateService;


    public static ArrayList<SecretKey> simetricniKLjucev = new ArrayList<>();

    @GetMapping(path = "/helloClient")
    @PreAuthorize("hasRole('agent')")
    public ResponseEntity<String> helloClient() throws Exception{
        X509Certificate certificate = certificateService.findMyCertificate();
        StringWriter sw = new StringWriter();
        JcaPEMWriter pm = new JcaPEMWriter(sw);
        pm.writeObject(certificate);
        pm.close();
        return new ResponseEntity<>(sw.toString(), HttpStatus.OK);
    }

    @PostMapping(path = "/preMasterSecret")
    @PreAuthorize("hasRole('agent')")
    public ResponseEntity<String> preMasterSecret(@RequestBody String[] params) throws Exception{

        String premasterSecret = params[0];
        String clienCertificate = params[1];
        byte[] decodedCert = Base64.getDecoder().decode(clienCertificate);
        byte[] decodedPremasterSecret = Base64.getDecoder().decode(premasterSecret);

        // constructcertificate from bytes
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        InputStream in = new ByteArrayInputStream(decodedCert);
        X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);

        SecretKey key = logService.processPreMasterSecret(cert, decodedPremasterSecret);

        //TODO: NACI BOLJI NACIN DA SE CUVJAU OVI SIMETRICNI KLJUCEVI KOJI SU DOFOVORENI..
        simetricniKLjucev.add(key);

        byte[] encriptedSimetricForChack = logService.encriptSimetricKey(key, cert.getPublicKey());
        byte[] encoded = Base64.getEncoder().encode(encriptedSimetricForChack);
        String secretKey = new String(encoded);

        return new ResponseEntity<>(secretKey, HttpStatus.OK);
    }

    @PostMapping(path = "/data")
    @PreAuthorize("hasRole('agent')")
    public  ResponseEntity<String> preMasterSecret(@RequestBody String data) throws Exception{

        byte[] decodedData = Base64.getDecoder().decode(data);
        String stillChipered = new String(decodedData);
        System.out.println("nedesifrovano: " + stillChipered);

        //@TODO: OVO MORA DA SE RESI OVO JE SAMO ZA DEMONSTRACIJU DA MOZE OVAKO
        SecretKey key = simetricniKLjucev.get(0);
        byte[] deciphered = cipherService.decrypt(key, decodedData);
        System.out.println("desifrovano: " + new String(deciphered));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/findAll")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<Log>> findAll() {
        return new ResponseEntity<>(logService.findAll(), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasRole('agent')")
    public ResponseEntity<Void> saveLogs(@RequestBody List<Log> logs) {
        this.logService.saveLogs(logs);
        return new ResponseEntity(HttpStatus.OK);
    }




}
