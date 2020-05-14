package rs.ac.uns.ftn.siemagent.config;

import org.apache.http.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.TrustManagerFactory;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class TrustStrategyConfig {

//    @Bean
//    public TrustStrategy getTrustStrategy() {
//        return new TrustStrategy() {
//            @Override
//            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                for(X509Certificate certificate : chain) {
//                    System.out.println("proveravam");
//                }
//
//                System.out.println("radiiiii");
//                return false;
//            }
//        };
//    }
}
