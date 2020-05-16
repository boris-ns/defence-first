package rs.ac.uns.ftn.siemcentar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.siemcentar.dto.request.LogFilterDTO;
import rs.ac.uns.ftn.siemcentar.dto.response.LogDTO;
import rs.ac.uns.ftn.siemcentar.mapper.LogMapper;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.model.LogType;
import rs.ac.uns.ftn.siemcentar.service.CertificateService;
import rs.ac.uns.ftn.siemcentar.service.CipherService;
import rs.ac.uns.ftn.siemcentar.service.LogService;
import rs.ac.uns.ftn.siemcentar.service.OCSPService;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

@RestController
@RequestMapping(value = "/api/log")
public class LogController {


    @Autowired
    private LogService logService;

    @Autowired
    private CipherService cipherService;

    @Autowired
    private CertificateService certificateService;


    public static HashMap<String, SecretKey> simetricniKLjucevi = new HashMap<>();

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
    public ResponseEntity<String[]> preMasterSecret(@RequestBody String[] params, HttpServletRequest request) throws Exception{

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

        byte[] criptedToken = null;
        String keyToken = null;
        while (true) {
            String token = cipherService.generateSafeToken();
            criptedToken = cipherService.encrypt(key, token.getBytes());
            byte[] encodedToken = Base64.getEncoder().encode(criptedToken);
            keyToken = new String(encodedToken);
            if (!simetricniKLjucevi.containsKey(keyToken)) {
                simetricniKLjucevi.put(keyToken, key);
                break;
            }
        }
        // cripted token ce da se salje kao header izmedju da bi se znalo koji kljuc iz mape da se uzme.


        byte[] encriptedSimetricForChack = logService.encriptSimetricKey(key, cert.getPublicKey());
        byte[] encoded = Base64.getEncoder().encode(encriptedSimetricForChack);
        String secretKey = new String(encoded);
        byte[] encodedToken = Base64.getEncoder().encode(criptedToken);


        String[] retVal = new String[2];
        retVal[0] = secretKey;
        retVal[1] = keyToken;

        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PostMapping(path = "/data")
    @PreAuthorize("hasRole('agent')")
    public  ResponseEntity<String> preMasterSecret(@RequestBody String data, HttpServletRequest request) throws Exception{

        byte[] decodedData = Base64.getDecoder().decode(data);
        String stillChipered = new String(decodedData);
        System.out.println("nedesifrovano: " + stillChipered);

        //@TODO: OVO MORA DA SE RESI OVO JE SAMO ZA DEMONSTRACIJU DA MOZE OVAKO
        String keyId = request.getHeader("HTTPS_session");
        SecretKey key = simetricniKLjucevi.get(keyId);

        if(key == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        byte[] deciphered = cipherService.decrypt(key, decodedData);
        ArrayList<String> logs = logService.convertLogsFromByte(deciphered);
        System.out.println("desifrovano: ");

        List<Log> logList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for(String l: logs) {
            System.out.println(l);
            logList.add(mapper.readValue(l, Log.class));
        }
        this.logService.saveLogs(logList);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/findAll")
    @PreAuthorize("hasRole('admin') or hasRole('operator')")
    public ResponseEntity<List<Log>> findAll() {
        return new ResponseEntity<>(logService.findAll(), HttpStatus.OK);
    }

    @PostMapping(path = "/filter")
    @PreAuthorize("hasRole('admin') or hasRole('operator')")
    public ResponseEntity<List<LogDTO>> filter(@RequestBody LogFilterDTO filter) {
        List<Log> logs = logService.searchAndFilter(filter);
        return new ResponseEntity<>(LogMapper.toListDto(logs), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasRole('agent')")
    public ResponseEntity<Void> saveLogs(@RequestBody List<Log> logs) {
        this.logService.saveLogs(logs);
        return new ResponseEntity(HttpStatus.OK);
    }

}
