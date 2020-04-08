package rs.ac.uns.ftn.siemagent.service.impl;

import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.ocsp.OCSPResponse;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.ocsp.*;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
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

import java.lang.reflect.Array;
import java.math.BigInteger;
import rs.ac.uns.ftn.siemagent.repository.Keystore;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;

@Service
public class OCSPServiceImpl implements OCSPService {

    @Value("${keystore.password}")
    private String keyStorePassword;

    @Value("${uri.pki.ocspReqURL}")
    private String ocspReqURL;

    @Value("${rootCASerialNumber}")
    private String rootCASerialNumber;

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
    public OCSPResp sendOCSPRequest(OCSPReq ocspReq) throws Exception{

        byte[] ocspBytes = ocspReq.getEncoded();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> ocspResponse = restTemplate.postForEntity(ocspReqURL, ocspBytes, byte[].class);

        OCSPResp ocspResp = new OCSPResp(OCSPResponse.getInstance(ocspResponse.getBody()));
        return ocspResp;
    }

    public boolean processOCSPResponse(OCSPReq ocspReq, OCSPResp ocspResp) throws Exception {
        if(!(ocspResp.getStatus() == OCSPRespBuilder.SUCCESSFUL)){
            throw new Exception("ocspResp now good overall");
        }
        BasicOCSPResp basicResponse = (BasicOCSPResp)ocspResp.getResponseObject();

        X509Certificate rootCA = certificateService.getCertificateBySerialNumber(rootCASerialNumber);

        ContentVerifierProvider prov = new JcaContentVerifierProviderBuilder().build(rootCA.getPublicKey());
        boolean signatureValid = basicResponse.isSignatureValid(prov);

        if(!signatureValid) {
            throw new Exception("ocspResp signature corupted");
        }

        // CHECK nonce Extension to prevent replay attack
        byte[] reqNonce = ocspReq.getExtension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce).getEncoded();
        byte[] respNonce = basicResponse.getExtension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce).getEncoded();

        if(!Arrays.equals(reqNonce,respNonce)) {
            throw new Exception("nonce extension not same");
        }

        boolean allGood = true;

        for(SingleResp response: basicResponse.getResponses()) {
            if (response.getCertStatus() != CertificateStatus.GOOD) {
                allGood = false;
            }
        }
        return allGood;
    }

}