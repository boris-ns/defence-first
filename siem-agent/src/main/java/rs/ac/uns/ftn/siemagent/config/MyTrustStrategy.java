package rs.ac.uns.ftn.siemagent.config;

import org.apache.http.ssl.TrustStrategy;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class MyTrustStrategy implements TrustStrategy {


    
    @Override
    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for(X509Certificate certificate : chain) {
            System.out.println(certificate.getSerialNumber());
        }
        return true;
    }
}
