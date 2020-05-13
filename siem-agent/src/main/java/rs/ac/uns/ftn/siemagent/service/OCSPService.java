package rs.ac.uns.ftn.siemagent.service;

import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPResp;

import java.security.cert.X509Certificate;

public interface OCSPService {

    OCSPReq generateOCSPRequest(X509Certificate certificate) throws Exception;

    OCSPResp sendOCSPRequest(OCSPReq ocspReq) throws Exception;

    public boolean processOCSPResponse(OCSPReq ocspReq, OCSPResp ocspResp) throws Exception;

}

