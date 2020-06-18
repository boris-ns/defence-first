package rs.ac.uns.ftn.siemagent.config;

import org.apache.http.ssl.TrustStrategy;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.siemagent.Constants.Constants;
import rs.ac.uns.ftn.siemagent.service.OCSPService;

import java.security.KeyStore;
import java.security.cert.*;
import java.util.Arrays;

@Component
public class MyTrustStrategy implements TrustStrategy {

    private KeyStore trustStore;

    private OCSPService ocspService;

    public MyTrustStrategy(){}

    public  MyTrustStrategy(KeyStore keyStore, OCSPService ocspService) {
        this.trustStore = keyStore;
        this.ocspService = ocspService;
    }

    @Override
    public boolean isTrusted(X509Certificate[] chain, String authType) {

        boolean retVal = true;

        // samo da isprinta lanac
        for(X509Certificate c : chain) {
            System.out.println(c.getSerialNumber());
        }

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            CertPath certPath = cf.generateCertPath(Arrays.asList(chain));
            CertPathValidator validator = CertPathValidator.getInstance("PKIX");
            PKIXParameters params = new PKIXParameters(this.trustStore);
            params.setRevocationEnabled(false);
            PKIXCertPathValidatorResult result = (PKIXCertPathValidatorResult) validator.validate(certPath, params);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            retVal = false;
        }finally {
            X509Certificate cert = chain[0];
            String name = cert.getSubjectX500Principal().getName();
            System.out.println(name);
            if (name.equals(Constants.PKI_COMMUNICATION_CERT_NAME))
            {
                return true;
            }
            else{
                try {
                    OCSPReq request = ocspService.generateOCSPRequest(chain);
                    OCSPResp response = ocspService.sendOCSPRequest(request);
                    boolean val = ocspService.processOCSPResponse(request,response);
                    retVal = val;
                }catch (Exception e) {
                    e.printStackTrace();
                    retVal = false;
                }
                finally {
                    return retVal;
                }
            }
        }
    }
}
