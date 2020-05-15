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
public class TrustStoreConfiguration {

    @Value("${keystore.password}")
    private String keyStorePassword;

    @Autowired
    @Qualifier("myKeyStore")
    private KeyStore keystore;

    @Autowired @Lazy
    private OCSPService ocspService;

    @Bean
    public SSLContext getCustomSSLContext() throws Exception{
//
//        System.out.println("evo ga custom SSLContext");
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(
//                TrustManagerFactory.getDefaultAlgorithm());
//                    // Initialise the TMF as you normally would, for example:
//        tmf.init((KeyStore)null);
//        TrustManager[] trustManagers = tmf.getTrustManagers();
//        final X509TrustManager origTrustmanager = (X509TrustManager)trustManagers[0];
//
//        TrustManager[] wrappedTrustManagers = new TrustManager[] {
//                new X509TrustManager() {
//                    @Override
//                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                        System.out.println("jebavam klijenta");
//                        return origTrustmanager.getAcceptedIssuers();
//                    }
//                    @Override
//                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
//                        System.out.println("jebavam klijenta");
//                        origTrustmanager.checkClientTrusted(certs, authType);
//                    }
//                    @Override
//                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
//                        //add more my logical code to validate CRL
//                        System.out.println("jebavam gde se kacim");
//                        origTrustmanager.checkServerTrusted(certs, authType);
//                    }
//                }
//        };
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//        sslContext.init(null, wrappedTrustManagers, null);


        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(
                        null, new MyTrustStrategy(keystore, ocspService))
                .loadKeyMaterial(keystore, keyStorePassword.toCharArray()).build();

        return sslContext;
    }



}
