package rs.ac.uns.ftn.siemcentar.configuration;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Security;

@Configuration
public class CertificateBuilder {

    @Bean
    public JcaContentSignerBuilder getBuilder(){
        Security.addProvider(new BouncyCastleProvider());
        JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
        builder = builder.setProvider("BC");
        return builder;
    }
}
