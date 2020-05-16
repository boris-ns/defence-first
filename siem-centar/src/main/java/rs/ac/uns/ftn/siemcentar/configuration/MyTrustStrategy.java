package rs.ac.uns.ftn.siemcentar.configuration;

import org.apache.http.ssl.TrustStrategy;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.siemcentar.service.OCSPService;
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

        for(X509Certificate c : chain) {
            System.out.println(c.getSerialNumber());
        }

        // da mora da se prijavi samo sa sertifikatom
        if (chain.length < 1) {return  false;}


        X509Certificate pki_cert = null;
        // provera ako je lanac duzine 1 i da li to prica sa PKI-em
        try {
            pki_cert = (X509Certificate) this.trustStore.getCertificate("pki");
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (chain.length == 1) {
            X509Certificate cert = (X509Certificate) chain[0];
            if (cert.getSerialNumber() != pki_cert.getSerialNumber() ||
                    !Arrays.equals(cert.getSignature(), pki_cert.getSignature())) {
                retVal = false;
            }
        }
        // ako je lanac duzi od 1 onda se pravi OCS request i salje se PKI-u
        else {
            try {
                OCSPReq request = ocspService.generateOCSPRequest(chain);
                OCSPResp response = ocspService.sendOCSPRequest(request);
                boolean val = ocspService.processOCSPResponse(request,response);
                retVal = val;
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }
        return retVal;
    }

}
