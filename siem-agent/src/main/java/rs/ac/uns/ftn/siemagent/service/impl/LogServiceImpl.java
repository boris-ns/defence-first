package rs.ac.uns.ftn.siemagent.service.impl;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bouncycastle.asn1.ocsp.OCSPResponse;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.siemagent.Constants.Constants;
import rs.ac.uns.ftn.siemagent.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemagent.model.Log;
import rs.ac.uns.ftn.siemagent.repository.Keystore;
import rs.ac.uns.ftn.siemagent.service.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;

@Service
public class LogServiceImpl implements LogService {


    @Value("${uri.siem.centar.send.helloClient}")
    private String httpsSiemCentarHello;

    @Value("${uri.siem.centar.send.preMasterSecret}")
    private String httpsSiemCentarPreMasterSecret;

    @Value("${uri.siem.center.send.data}")
    private String httpSiemCentarData;


    @Value("${keystore.password}")
    private String keyStorePassword;

    @Autowired
    private AuthService authService;

    @Autowired
    private OCSPService ocspService;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private KeyPairGeneratorService keyPairGeneratorService;

    @Autowired
    private Keystore keystore;

    @Autowired
    private CipherService cipherService;


    @Override
    public void sendLogs(TokenDTO token, SecretKey simetricKey, String secureToken, ArrayList<Log> logs) {
        try {

            // ZAKRIPTUJE listu logova i posalje kao string
            byte[] logsInByte = convertToByteArray(logs);
            byte[] criptedMessage = cipherService.encrypt(simetricKey, logsInByte);
            String base64Message = new String(Base64.getEncoder().encode(criptedMessage));

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "bearer " + token.getAccesss_token());
            // da zna koji simetricni kljuc da koristi SiemServer
            headers.set("HTTPS_session", secureToken);


            HttpEntity<String> entityReq = new HttpEntity(base64Message, headers);
            ResponseEntity<String> certificate = null;

            try {
                certificate = restTemplate.exchange(httpSiemCentarData, HttpMethod.POST, entityReq, String.class);
            } catch (HttpClientErrorException e) {
                System.out.println("[ERROR] You are not allowed to make CSR request");
            }

            // Ovo znaci da je istekao token, pa cemo refreshovati token
            // i opet poslati zahtev
            // @TODO: Nije testirano
            if (certificate.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                token = authService.refreshToken(token.getRefresh_token());
                headers.set("Authorization", "bearer " + token.getAccesss_token());
                certificate = restTemplate.exchange(httpSiemCentarData, HttpMethod.POST, entityReq, String.class);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public X509Certificate sendSiemCenterHello(TokenDTO token) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "bearer " + token.getAccesss_token());


        HttpEntity<Void> entityReq = new HttpEntity<>(headers);
        ResponseEntity<String> certificate = null;

        try {
            certificate = restTemplate.exchange(httpsSiemCentarHello, HttpMethod.GET, entityReq, String.class);
        } catch (HttpClientErrorException e) {
            System.out.println("[ERROR] You are not allowed to make CSR request");
            return null;
        }

        // Ovo znaci da je istekao token, pa cemo refreshovati token
        // i opet poslati zahtev
        // @TODO: Nije testirano
        if (certificate.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
            token = authService.refreshToken(token.getRefresh_token());
            headers.set("Authorization", "bearer " + token.getAccesss_token());
            certificate = restTemplate.exchange(httpsSiemCentarHello, HttpMethod.POST, entityReq, String.class);
        }

        PEMParser pemParser = new PEMParser(new StringReader(certificate.getBody()));
        X509CertificateHolder certificateHolder = (X509CertificateHolder) pemParser.readObject();


        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
        certConverter = certConverter.setProvider("BC");
        return certConverter.getCertificate(certificateHolder);
    }

    @Override
    public Object[] initCommunicationWithSiemCentar(TokenDTO token) throws Exception {

        Object[] retVal = new Object[2];
        SecretKey secretKey = keyPairGeneratorService.generateSimetricKey();

        X509Certificate centerCertificate = this.sendSiemCenterHello(token);
        OCSPReq request = ocspService.generateOCSPRequest(centerCertificate, token);
		OCSPResp response = ocspService.sendOCSPRequest(request, token);
		boolean val = ocspService.processOCSPResponse(request,response, token);

		if(val) {
		    System.out.println("validan je sertifikat servera");

		    byte simetricKey[] = this.preMasterSecret(centerCertificate, secretKey);
		    String secureToken = this.sendPreMasterSecret(token, simetricKey);

            retVal[0] = secretKey;
            retVal[1] = secureToken;
            return retVal;
        }
		else{
		    System.out.println("nije validan sertifikat servera");
		    return null;
        }

    }



    @Override
    public String sendPreMasterSecret(TokenDTO token, byte[] simetricKey) throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "bearer " + token.getAccesss_token());


        byte[] encoded = Base64.getEncoder().encode(simetricKey);
        String secretKey = new String(encoded);

        X509Certificate myCertificate = certificateService.findMyCertificate();
        byte[] encodedCert = Base64.getEncoder().encode(myCertificate.getEncoded());
        String myCert = new String(encodedCert);

        // da posalje svoj sertifikat i da posalje simetricni kljuc koji je zakriptovan
        String[] params = new String[2];
        params[0] = secretKey;
        params[1] = myCert;

        HttpEntity<String[]> entityReq = new HttpEntity<>(params, headers);
        ResponseEntity<String[]> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(httpsSiemCentarPreMasterSecret, HttpMethod.POST, entityReq, String[].class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
//            return null;
        }

        // Ovo znaci da je istekao token, pa cemo refreshovati token
        // i opet poslati zahtev
        // @TODO: Nije testirano
        if (responseEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
            token = authService.refreshToken(token.getRefresh_token());
            headers.set("Authorization", "bearer " + token.getAccesss_token());
            responseEntity = restTemplate.exchange(httpsSiemCentarPreMasterSecret, HttpMethod.POST, entityReq, String[].class);
        }

        String key = responseEntity.getBody()[0];
        String secretToken = responseEntity.getBody()[1];
        byte[] decodedPremasterSecret = Base64.getDecoder().decode(key);
        byte[] decriptedKey = decpritWithMyPrivate(decodedPremasterSecret);
        SecretKey originalKey = new SecretKeySpec(decriptedKey, 0, decriptedKey.length, "AES");


        return secretToken;
    }

    @Override
    public byte[] preMasterSecret(X509Certificate certificateCenter, SecretKey secretKey) throws Exception {
        return cipherService.encrypt(certificateCenter.getPublicKey(), secretKey.getEncoded());
    }


    public byte[] decpritWithMyPrivate(byte[] data) throws Exception{
        PrivateKey myPrivateKey = keystore.readPrivateKey(Constants.KEY_PAIR_ALIAS, keyStorePassword);
        return  cipherService.decrypt(myPrivateKey, data);
    }


    private byte[] convertToByteArray(ArrayList<Log> logs) throws IOException {

        ArrayList<String> stringLogs = new ArrayList<>();
        for( Log l : logs) {
            stringLogs.add(l.toString());
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(stringLogs);
            out.flush();
            return bos.toByteArray();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }






}
