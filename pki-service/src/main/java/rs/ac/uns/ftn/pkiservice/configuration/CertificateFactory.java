package rs.ac.uns.ftn.pkiservice.configuration;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.security.Security;
import java.security.cert.CertificateException;

@Configuration
public class CertificateFactory {

    @Bean
    public java.security.cert.CertificateFactory getFactory() throws CertificateException {
        Security.addProvider(new BouncyCastleProvider());
        java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");
        return cf;
    }
}
