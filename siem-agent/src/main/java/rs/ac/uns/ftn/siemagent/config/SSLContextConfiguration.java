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
