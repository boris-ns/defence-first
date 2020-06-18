package rs.ac.uns.ftn.siemcentar.configuration;

import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.SslStoreProvider;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.siemcentar.repository.Keystore;
import rs.ac.uns.ftn.siemcentar.service.OCSPService;

import javax.net.ssl.*;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class SSLContextConfiguration {

    @Value("${keystore.password}")
    private String keyStorePassword;

    @Autowired
    @Qualifier("myKeyStore")
    private KeyStore keystore;

    @Autowired
    @Qualifier("trustStore")
    private KeyStore trustStore;

    @Autowired @Lazy
    private OCSPService ocspService;


    @Bean(name = "myContext")
    public SSLContext getCustomSSLContext() throws Exception{

        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(
                        keystore, new MyTrustStrategy(trustStore, ocspService))
                .loadKeyMaterial(keystore, keyStorePassword.toCharArray()).build();

        return sslContext;
    }



}
