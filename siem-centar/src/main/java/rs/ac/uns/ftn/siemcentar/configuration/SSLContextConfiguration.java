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


    @Bean
    public SSLContext getCustomSSLContext() throws Exception{

//        System.out.println("evo ga custom SSLContext");
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(
//                TrustManagerFactory.getDefaultAlgorithm());
//                    // Initialise the TMF as you normally would, for example:
//        tmf.init((KeyStore)null);
//        TrustManager[] trustManagers = tmf.getTrustManagers();
//        final X509TrustManager origTrustmanager = (X509TrustManager)trustManagers[0];

        TrustManagerCustomImpl trm = new TrustManagerCustomImpl(trustStore, ocspService);
        TrustManager[] wrappedTrustManagers = new TrustManager[1];
        wrappedTrustManagers[0] = trm;


        char[] passwordForAllPrivateKeys = keyStorePassword.toCharArray(); // cannot be null
        String algorithm = KeyManagerFactory.getDefaultAlgorithm(); // returns "SunX509" by default in 1.8
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
        kmf.init(keystore, passwordForAllPrivateKeys);
        KeyManager[] keyManagers = kmf.getKeyManagers();


        SSLContext sslContext = SSLContexts.createDefault();
        sslContext.init(keyManagers, wrappedTrustManagers, new SecureRandom());



//        SSLContext sslContext = SSLContexts.custom()
//                .loadTrustMaterial(
//                        keystore, new MyTrustStrategy(trustStore, ocspService))
//                .loadKeyMaterial(keystore, keyStorePassword.toCharArray()).build();

        return sslContext;
    }



}
