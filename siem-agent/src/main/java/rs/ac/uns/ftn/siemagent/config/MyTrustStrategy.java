package rs.ac.uns.ftn.siemagent.config;

import org.apache.http.ssl.TrustStrategy;
import org.springframework.stereotype.Component;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.*;
import java.util.Arrays;

@Component
public class MyTrustStrategy implements TrustStrategy {


    // sto je ovo Null...
    private KeyStore trustStore;

    public MyTrustStrategy(){}

    public  MyTrustStrategy(KeyStore keyStore) {
        this.trustStore = keyStore;
    }

    @Override
    public boolean isTrusted(X509Certificate[] chain, String authType) {
        boolean retVal = true;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            CertPath path = cf.generateCertPath(Arrays.asList(chain));
            CertPathValidator validator = CertPathValidator.getInstance("PKIX");
            PKIXParameters params = new PKIXParameters(trustStore);
            params.setRevocationEnabled(false);
            PKIXCertPathValidatorResult r = (PKIXCertPathValidatorResult) validator.validate(path, params);
        }
        catch (NoSuchAlgorithmException | CertificateException | KeyStoreException | InvalidAlgorithmParameterException e){e.printStackTrace();}
        catch (CertPathValidatorException e){
            e.printStackTrace();
            retVal = false;
        }finally {
            return retVal;
        }

//        for(X509Certificate certificate : chain) {
//            System.out.println(certificate.getSerialNumber());
//        }
//        return true;
    }
}
