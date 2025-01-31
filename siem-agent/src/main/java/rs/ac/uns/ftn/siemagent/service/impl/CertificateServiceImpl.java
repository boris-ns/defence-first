package rs.ac.uns.ftn.siemagent.service.impl;

import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import rs.ac.uns.ftn.siemagent.Constants.Constants;
import rs.ac.uns.ftn.siemagent.config.AgentConfiguration;
import rs.ac.uns.ftn.siemagent.config.CertificateBuilder;
import rs.ac.uns.ftn.siemagent.repository.Keystore;
import rs.ac.uns.ftn.siemagent.service.CertificateService;
import rs.ac.uns.ftn.siemagent.service.KeyPairGeneratorService;

import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Value("${uri.pki.createCertificate}")
    private String createCertificateURL;

    @Value("${uri.pki.getCertificate}")
    private String getCertificateURL;

    @Value("${uri.pki.renewCertificate}")
    private String renewCertificate;

    @Value("${issuerSerialNumber}")
    private String issuerSerialNumber;

    @Value("${keystore.password}")
    private String keyStorePassword;

    @Value("${my.certificate.path}")
    private String pathToCertificate;

    @Value("${ssl.path}")
    private String sslPath;

    @Autowired
    private AgentConfiguration agentConfiguration;

    @Autowired
    private KeyPairGeneratorService keyPairGeneratorService;

    @Autowired
    private Keystore keystore;

    @Autowired
    private CertificateBuilder certificateBuilder;

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @Override
    public void installCertificateFromFile() throws Exception {
        ArrayList<Certificate> certificates = new ArrayList<>();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        String retVal = readAllBytesJava7(pathToCertificate);
        String[] cet = retVal.split(",");
        for (String c : cet) {
            InputStream inputStream = new ByteArrayInputStream(c.getBytes());
            Certificate cert = cf.generateCertificate(inputStream);
            certificates.add(cert);
        }

        Certificate[] certificatesArray = new Certificate[certificates.size()];
        for(int i=0; i <certificates.size();i++) {
            certificatesArray[i] = certificates.get(i);
        }
        CertPath certPath = cf.generateCertPath(certificates);


        PrivateKey privateKey = keystore.readPrivateKey(Constants.KEY_PAIR_ALIAS, keyStorePassword);
        keystore.writeChain(Constants.CERTIFICATE_ALIAS, privateKey,
                keyStorePassword.toCharArray(), certificatesArray);

    }

    @Override
    public X509Certificate findMyCertificate() throws Exception{
        return (X509Certificate) keystore.readMyCertificate();
    }


    @Override
    public String buildCertificateRequest(KeyPair certKeyPair, PrivateKey signCerPrivateKey, Boolean renewal) throws Exception {


        String myCertId = "";
        X509Certificate certificate = (X509Certificate) keystore.getKeyStore().getCertificate(Constants.CERTIFICATE_ALIAS);
        if(certificate!=null) {
            myCertId = certificate.getSerialNumber().toString();
        }


        X500Principal principal = buildSertificateSubjetPrincipal();


        PKCS10CertificationRequestBuilder p10Builder =
                new JcaPKCS10CertificationRequestBuilder(principal, certKeyPair.getPublic());

        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
        ContentSigner signer = null;

        //@TODO: dodati jos atributa ako zatreba
        // da prosledimo koji je SerialNumber o
        GeneralNames subjectAltName = null;
        GeneralName issuerSNName = new GeneralName(GeneralName.dNSName, issuerSerialNumber);
        GeneralName myCertSerialNumber = new GeneralName(GeneralName.uniformResourceIdentifier, "1593008519115");

        if(renewal) {
            subjectAltName = new GeneralNames(new GeneralName[]{issuerSNName, myCertSerialNumber});
        }else {
            subjectAltName = new GeneralNames(new GeneralName[]{issuerSNName});
        }

//        toDer
//        // da pretvorim id od IssuerCert u ovo sto treba za ekstenziju...
//        ASN1InputStream aIn = new ASN1InputStream("aaa".getBytes());
//        ASN1Sequence asn1Sequence = ASN1Sequence.getInstance(aIn);
//
//        AuthorityInformationAccess authorityInformationAccess =
//                AuthorityInformationAccess.getInstance(asn1Sequence);
        p10Builder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, subjectAltName);
        try {
            signer = csBuilder.build(signCerPrivateKey);
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        }

        PKCS10CertificationRequest csr = p10Builder.build(signer);
        StringWriter stringWriter = new StringWriter();

        try {
            JcaPEMWriter writer = new JcaPEMWriter(stringWriter);
            writer.writeObject(csr);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.saveKeyPair(certKeyPair, renewal);
        return stringWriter.toString();
    }

    @Override
    public void createRequestForCertificate() throws Exception {
        KeyPair pair = keyPairGeneratorService.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        String csr = this.buildCertificateRequest(pair, privateKey, false);

        String fileName = "cert_request.csr";
        String path = sslPath + "/" + fileName;

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(csr);
            writer.close();
        }
    }

    @Override
    public X500Principal buildSertificateSubjetPrincipal() {
        String name = "CN=" + agentConfiguration.getName();
        HashMap<String, String> params = new HashMap<>();
        X500Principal a = new X500Principal(name, params);
        return a;
    }

    @Override
    public boolean checkSertificate(X509Certificate certificate) {
        return false;
    }



    public void saveKeyPair(KeyPair keyPair, Boolean renewal) throws Exception {
        X509Certificate certificate =  createSertificateForKeyPair(keyPair);
        if (renewal) {
            keystore.write(Constants.RENEWAL_KEY_PAIR_ALIAS, keyPair.getPrivate(), keyStorePassword.toCharArray(), certificate);
        }else{
            keystore.write(Constants.KEY_PAIR_ALIAS, keyPair.getPrivate(), keyStorePassword.toCharArray(), certificate);
        }


    }

    @Override
    public X509Certificate getCertificateBySerialNumber(String serialNumber) throws Exception{
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entityReq = new HttpEntity<>(serialNumber, headers);
        ResponseEntity<String> certificate = null;

        try {
            certificate = restTemplate.exchange(getCertificateURL + "/" + serialNumber, HttpMethod.GET, entityReq, String.class);
        } catch (HttpClientErrorException e) {
            System.out.println("[ERROR] You are not allowed to make check request");
            return null;
        }

        PEMParser pemParser = new PEMParser(new StringReader(certificate.getBody()));
        X509CertificateHolder certificateHolder = (X509CertificateHolder) pemParser.readObject();

        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
        certConverter = certConverter.setProvider("BC");
        return certConverter.getCertificate(certificateHolder);
    }

    @Override
    public void createReplaceCertificateRequest() throws Exception {

        KeyPair pair = keyPairGeneratorService.generateKeyPair();
        PrivateKey privateKey = keystore.readPrivateKey(Constants.KEY_PAIR_ALIAS, keyStorePassword);
        String csr = this.buildCertificateRequest(pair, privateKey, true);

        String fileName = "cert_request_renewal.csr";
        String path = sslPath + "/" + fileName;

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(csr);
        }
    }

    public X509Certificate createSertificateForKeyPair(KeyPair keyPair) throws Exception{
        JcaContentSignerBuilder builder = certificateBuilder.getBuilder();
        ContentSigner contentSigner = builder.build(keyPair.getPrivate());
        X500Principal p = this.buildSertificateSubjetPrincipal();
        X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
                p,
                new BigInteger(String.valueOf(1)),
                new Date(),
                new Date(),p,
                keyPair.getPublic());
        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
        certConverter = certConverter.setProvider("BC");
        return certConverter.getCertificate(certGen.build(contentSigner));
    }

    @Override
    public InputStream fullStream(String certFile) throws IOException{
        FileInputStream fis = new FileInputStream(certFile);
        DataInputStream dis = new DataInputStream(fis);
        byte[] bytes = new byte[dis.available()];
        dis.readFully(bytes);
        return new ByteArrayInputStream(bytes);
    }

    private static String readAllBytesJava7(String filePath)
    {
        String content = "";
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return content;
    }

}
