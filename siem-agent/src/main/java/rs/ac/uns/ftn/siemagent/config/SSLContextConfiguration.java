package rs.ac.uns.ftn.siemagent.config;

import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import rs.ac.uns.ftn.siemagent.service.OCSPService;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyStore;
import java.security.SecureRandom;

@Configuration
public class SSLContextConfiguration {

    @Value("${keystore.password}")
    private String keyStorePassword;

    @Autowired
    @Qualifier("myKeyStore")
    private KeyStore keystore;

    @Autowired
    @Qualifier("myTrustStore")
    private KeyStore trustStore;

    @Autowired
    @Lazy
    private OCSPService ocspService;


    @Bean(name = "mySSLContext")
    public SSLContext getCustomSSLContext() throws Exception{

        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(
                        trustStore, new MyTrustStrategy(trustStore, ocspService))
                .loadKeyMaterial(keystore, keyStorePassword.toCharArray()).build();

        return sslContext;
    }



}
