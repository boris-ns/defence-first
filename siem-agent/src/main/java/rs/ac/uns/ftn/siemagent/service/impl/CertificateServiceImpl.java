package rs.ac.uns.ftn.siemagent.service.impl;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.x509.extension.X509ExtensionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.siemagent.Constants.Constants;
import rs.ac.uns.ftn.siemagent.config.AgentConfiguration;
import rs.ac.uns.ftn.siemagent.config.CertificateBuilder;
import rs.ac.uns.ftn.siemagent.repository.Keystore;
import rs.ac.uns.ftn.siemagent.service.CertificateService;
import rs.ac.uns.ftn.siemagent.service.KeyPairGeneratorService;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Value("${uri.pki.createCertificate}")
    private String createCertificateURL;

    @Value("${uri.pki.getCertificate}")
    private String getCertificateURL;

    @Value("${issuerSerialNumber}")
    private String issuerSerialNumber;

    @Value("${keystore.password}")
    private String keyStorePassword;

    @Autowired
    private AgentConfiguration agentConfiguration;

    @Autowired
    private KeyPairGeneratorService keyPairGeneratorService;

    @Autowired
    private Keystore keystore;

    @Autowired
    private CertificateBuilder certificateBuilder;

    @Override
    public String buildCertificateRequest() throws Exception {
        KeyPair pair = keyPairGeneratorService.generateKeyPair();

        X500Principal principal = buildSertificateSubjetPrincipal();

        PKCS10CertificationRequestBuilder p10Builder =
                new JcaPKCS10CertificationRequestBuilder(principal, pair.getPublic());

        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
        ContentSigner signer = null;

        // @TODO: Dodati atribute sa komandom kao ispod, valjda
        //


        // da prosledimo koji je SerialNumber o
        GeneralName issuerSNName = new GeneralName(GeneralName.dNSName, issuerSerialNumber);
        GeneralNames subjectAltName = new GeneralNames(issuerSNName);


//        toDer
//        // da pretvorim id od IssuerCert u ovo sto treba za ekstenziju...
//        ASN1InputStream aIn = new ASN1InputStream("aaa".getBytes());
//        ASN1Sequence asn1Sequence = ASN1Sequence.getInstance(aIn);
//
//        AuthorityInformationAccess authorityInformationAccess =
//                AuthorityInformationAccess.getInstance(asn1Sequence);
        p10Builder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, subjectAltName);
        try {
            signer = csBuilder.build(pair.getPrivate());
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

        System.out.println(pair.getPublic());
        System.out.println(stringWriter.toString());

        this.sendRequestForCertificate(stringWriter.toString());

        this.saveKeyPair(pair);
        return stringWriter.toString();
    }

    @Override
    public X500Principal buildSertificateSubjetPrincipal() {

        String name = "CN=" + agentConfiguration.getName();
        HashMap<String, String> params = new HashMap<>();
        X500Principal a = new X500Principal(name, params);
        return a;
        //@TODO: ako bude trebalo nesto od ovih polja VEZANIH ZA SUBJEKTA, podaci iz app.properties

//
//        builder.addRDN(BCStyle.CN, "Goran Sladic");
//        builder.addRDN(BCStyle.SURNAME, "Sladic");
//        builder.addRDN(BCStyle.GIVENNAME, "Goran");
//        builder.addRDN(BCStyle.O, "UNS-FTN");
//        builder.addRDN(BCStyle.OU, "Katedra za informatiku");
//        builder.addRDN(BCStyle.C, "RS");
//        builder.addRDN(BCStyle.E, "sladicg@uns.ac.rs");
//        // @TODO: UID ovo da bude MAC adresa ili tako nesto?
//        builder.addRDN(BCStyle.UID, "123456");
//        return name;
    }

    @Override
    public boolean checkSertificate(X509Certificate certificate) {
        return false;
    }

    private String sendRequestForCertificate(String csr) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> certificate = restTemplate.postForEntity(createCertificateURL, csr, String.class);
        return certificate.getBody();
    }


    public void saveKeyPair(KeyPair keyPair) throws Exception {
        X509Certificate certificate =  createSertificateForKeyPair(keyPair);
        keystore.write(Constants.KEY_PAIR_ALIAS, keyPair.getPrivate(), keyStorePassword.toCharArray(), certificate);

    }


    @Override
    public X509Certificate getCertificateBySerialNumber(String serialNumber) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> certificateStr = restTemplate.getForEntity(getCertificateURL + "/" + serialNumber, String.class);

        PEMParser pemParser = new PEMParser(new StringReader(certificateStr.getBody()));
        X509CertificateHolder certificateHolder = (X509CertificateHolder) pemParser.readObject();

        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
        certConverter = certConverter.setProvider("BC");
        return certConverter.getCertificate(certificateHolder);
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






}
