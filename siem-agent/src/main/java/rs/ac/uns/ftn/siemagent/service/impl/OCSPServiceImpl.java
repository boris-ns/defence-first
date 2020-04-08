package rs.ac.uns.ftn.siemagent.service.impl;

import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.ocsp.OCSPRequest;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.ocsp.CertificateID;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
import org.bouncycastle.openssl.MiscPEMGenerator;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObjectGenerator;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.siemagent.Constants.Constants;
import rs.ac.uns.ftn.siemagent.config.AgentConfiguration;
import rs.ac.uns.ftn.siemagent.config.CertificateBuilder;
import rs.ac.uns.ftn.siemagent.service.CertificateService;
import rs.ac.uns.ftn.siemagent.service.OCSPService;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import rs.ac.uns.ftn.siemagent.repository.Keystore;

import javax.security.auth.x500.X500Principal;
import javax.swing.text.html.parser.Entity;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;

@Service
public class OCSPServiceImpl implements OCSPService {

    @Value("${keystore.password}")
    private String keyStorePassword;

    @Value("${uri.pki.ocspReqURL}")
    private String ocspReqURL;

    @Autowired
    private Keystore keyStore;

    @Autowired
    private CertificateBuilder certificateBuilder;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private AgentConfiguration agentConfiguration;


    @Override
    public OCSPReq generateOCSPRequest(X509Certificate certificate)
            throws Exception {

        //@TODO: issuerCert is still mocked
        X509Certificate issuerCert = certificateService.getCertificateBySerialNumber("df.pki.root");

        BcDigestCalculatorProvider util = new BcDigestCalculatorProvider();

        // Generate the id for the certificate we are looking for
        CertificateID id = new CertificateID(util.get(  CertificateID.HASH_SHA1),
                new X509CertificateHolder(issuerCert.getEncoded()), certificate.getSerialNumber());

        OCSPReqBuilder ocspGen = new OCSPReqBuilder();
        ocspGen.addRequest(id);

        // nonce je vrednost koju onaj koji salje zahtev salje kako bi se stitili od replay napada.
        BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
        Extension ext = new Extension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce, true, new DEROctetString(nonce.toByteArray()));
        ocspGen.setRequestExtensions(new Extensions(new Extension[] { ext }));

        // dobavljanje privatnog kljuca i potpisivanje requesta
        JcaContentSignerBuilder builder = certificateBuilder.getBuilder();
        PrivateKey privateKey =  keyStore.readPrivateKey(Constants.KEY_PAIR_ALIAS, keyStorePassword);
        ContentSigner contentSigner = builder.build(privateKey);

        X500NameBuilder nameBuilder = new X500NameBuilder();
        nameBuilder.addRDN(BCStyle.CN, agentConfiguration.getName());
        nameBuilder.addRDN(BCStyle.UNIQUE_IDENTIFIER, certificate.getSerialNumber().toString());
        ocspGen.setRequestorName(nameBuilder.build());

        OCSPReq request = ocspGen.build(contentSigner, null);
        return request;
    }

    @Override
    public String sendOCSPRequest(OCSPReq ocspReq) throws Exception{

        byte[] ocspBytes = ocspReq.getEncoded();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> ocspResponse = restTemplate.postForEntity(ocspReqURL, ocspBytes, String.class);
        return ocspResponse.getBody();
    }
}
