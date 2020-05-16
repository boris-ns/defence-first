package rs.ac.uns.ftn.siemcentar.service.impl;

import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.ocsp.OCSPResponse;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Certificate;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.siemcentar.constants.Constants;
import rs.ac.uns.ftn.siemcentar.configuration.AgentConfiguration;
import rs.ac.uns.ftn.siemcentar.configuration.CertificateBuilder;
import rs.ac.uns.ftn.siemcentar.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemcentar.repository.Keystore;
import rs.ac.uns.ftn.siemcentar.service.AuthService;
import rs.ac.uns.ftn.siemcentar.service.CertificateService;
import rs.ac.uns.ftn.siemcentar.service.OCSPService;

import java.math.BigInteger;
import java.security.KeyStore;
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

    @Value("${mySerialNumber}")
    private String mySerialNumber;

    @Autowired
    private Keystore keyStore;

    @Autowired
    @Qualifier("trustStore")
    private KeyStore trustStore;

    @Autowired
    private CertificateBuilder certificateBuilder;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private AgentConfiguration agentConfiguration;

    @Autowired
    private AuthService authService;

    @Autowired @Lazy
    private RestTemplate restTemplate;

    @Override
    public OCSPReq generateOCSPRequest(X509Certificate[] certificates)
            throws Exception {

        X509Certificate certificate = certificates[0];
        X509Certificate issuerCert =  certificates[1];

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

        // @TODO: Treba resiti problem kada se renewuje sertifikat jer se tada vise ne koristi
        // KEY_PAIR_ALIAS vec onaj drugi za renew
        PrivateKey privateKey =  keyStore.readPrivateKey(Constants.KEY_PAIR_ALIAS, keyStorePassword);
        ContentSigner contentSigner = builder.build(privateKey);

        X500NameBuilder nameBuilder = new X500NameBuilder();
        nameBuilder.addRDN(BCStyle.CN, agentConfiguration.getName());
        nameBuilder.addRDN(BCStyle.UNIQUE_IDENTIFIER, mySerialNumber);
        ocspGen.setRequestorName(nameBuilder.build());

        OCSPReq request = ocspGen.build(contentSigner, null);
        return request;
    }

    @Override
    public OCSPResp sendOCSPRequest(OCSPReq ocspReq) throws Exception{
        HttpHeaders headers = new HttpHeaders();

        byte[] ocspBytes = ocspReq.getEncoded();
        HttpEntity<byte[]> entityReq = new HttpEntity<>(ocspBytes, headers);
        ResponseEntity<byte[]> ocspResponse = null;

        try {
            ocspResponse = restTemplate.exchange(ocspReqURL, HttpMethod.POST, entityReq, byte[].class);
        } catch (HttpClientErrorException e) {
            System.out.println("[ERROR] You are not allowed to make CSR request");
            return null;
        }
        OCSPResp ocspResp = new OCSPResp(OCSPResponse.getInstance(ocspResponse.getBody()));
        return ocspResp;
    }

    @Override
    public boolean processOCSPResponse(OCSPReq ocspReq, OCSPResp ocspResp) throws Exception {
        if(!(ocspResp.getStatus() == OCSPRespBuilder.SUCCESSFUL)){
            throw new Exception("ocspResp now good overall");
        }

        BasicOCSPResp basicResponse = (BasicOCSPResp)ocspResp.getResponseObject();

        X509Certificate rootCA = (X509Certificate) trustStore.getCertificate("pki");;

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
