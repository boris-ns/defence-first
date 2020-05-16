package rs.ac.uns.ftn.siemagent.config;

import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPResp;
import rs.ac.uns.ftn.siemagent.service.OCSPService;

import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;

public class TrustManagerCustomImpl implements X509TrustManager {

    private final Collection<X509Certificate> trustedCerts;

    private KeyStore trustStore;

    private OCSPService ocspService;

    public TrustManagerCustomImpl(KeyStore keyStore, OCSPService ocspService) throws KeyStoreException {
        trustedCerts = new HashSet<>();
        this.trustStore = keyStore;
        this.ocspService = ocspService;
        if (trustStore != null) {
            Enumeration<String> enumeration = trustStore.aliases();
            while(enumeration.hasMoreElements()) {
                String alias = enumeration.nextElement();
                X509Certificate certificate = (X509Certificate) trustStore.getCertificate(alias);
                trustedCerts.add(certificate);
            }
        }
        showTrustedCerts();
    }

    @Override
    public void checkClientTrusted(X509Certificate chain[], String authType)
            throws CertificateException {
        checkTrusted(chain, authType, true);
    }

    @Override
    public void checkServerTrusted(X509Certificate chain[], String authType)
            throws CertificateException {
        checkTrusted(chain, authType, false);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        X509Certificate[] certsArray = new X509Certificate[trustedCerts.size()];
        trustedCerts.toArray(certsArray);
        return certsArray;
    }


    private void checkTrusted(X509Certificate[] chain, String authType, boolean isClient) throws CertificateException {
        boolean retVal = true;


        System.out.println("dosao sammmmmmmm");
        System.out.println(isClient);

        for(X509Certificate c : chain) {
            System.out.println(c.getSerialNumber());
        }

        // da mora da se prijavi samo sa sertifikatom
        if (chain.length < 1) { throw new CertificateException();}


        X509Certificate pki_cert = null;
        // provera ako je lanac duzine 1 i da li to prica sa PKI-em
        try {
            pki_cert = (X509Certificate) this.trustStore.getCertificate("pki");
        }catch (Exception e) {
            e.printStackTrace();
            throw new CertificateException("pukao pri ucitavanju pki sertifikata");
        }
        if (chain.length == 1) {
            X509Certificate cert = (X509Certificate) chain[0];
            if (cert.getSerialNumber() == pki_cert.getSerialNumber() ||
                    !Arrays.equals(cert.getSignature(), pki_cert.getSignature())) {
                return;
            }
            else{
                try {
                    OCSPReq request = ocspService.generateOCSPRequest(chain);
                    OCSPResp response = ocspService.sendOCSPRequest(request);
                    boolean val = ocspService.processOCSPResponse(request,response);
                    if(!val) {
                        throw new CertificateException("bad OCSP response");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    throw new CertificateException(e.getMessage());
                }
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
                throw new CertificateException(e.getMessage());
            }

        }
    }




    private void showTrustedCerts() {
        if (true) {
            for (X509Certificate cert : trustedCerts) {
                System.out.println("adding as trusted cert:");
                System.out.println("  Subject: "
                        + cert.getSubjectX500Principal());
                System.out.println("  Issuer:  "
                        + cert.getIssuerX500Principal());
                System.out.println("  Algorithm: "
                        + cert.getPublicKey().getAlgorithm()
                        + "; Serial number: 0x"
                        + cert.getSerialNumber().toString(16));
                System.out.println("  Valid from "
                        + cert.getNotBefore() + " until "
                        + cert.getNotAfter());
                System.out.println();
            }
        }
    }

}

