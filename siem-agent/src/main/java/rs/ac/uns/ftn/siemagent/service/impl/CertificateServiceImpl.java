package rs.ac.uns.ftn.siemagent.service.impl;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.siemagent.config.AgentConfiguration;
import rs.ac.uns.ftn.siemagent.repository.Keystore;
import rs.ac.uns.ftn.siemagent.service.CertificateService;
import rs.ac.uns.ftn.siemagent.service.KeyPairGeneratorService;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyPair;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Value("${uri.pki.createCertificate}")
    private String createCertificateURL;

    @Autowired
    private AgentConfiguration agentConfiguration;

    @Autowired
    private KeyPairGeneratorService keyPairGeneratorService;

    @Autowired
    private Keystore keystore;

    @Override
    public String buildCertificateRequest() {
        KeyPair pair = keyPairGeneratorService.generateKeyPair();

        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
                new X500Principal("CN=" + agentConfiguration.getName()), pair.getPublic());

        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
        ContentSigner signer = null;

        // @TODO: Dodati atribute sa komandom kao ispod, valjda
//        p10Builder.addAttribute();

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

        String certificate = this.sendRequestForCertificate(stringWriter.toString());
        // @TODO upisati sertifikat i povezati ga sa napravljenim privatenim kljucem

        return stringWriter.toString();
    }

    private String sendRequestForCertificate(String csr) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> certificate = restTemplate.postForEntity(createCertificateURL, csr, String.class);
        return certificate.getBody();
    }

}
