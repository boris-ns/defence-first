package rs.ac.uns.ftn.siemagent.service;

import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPResp;
import rs.ac.uns.ftn.siemagent.dto.response.TokenDTO;

import java.security.cert.X509Certificate;

public interface OCSPService {

    OCSPReq generateOCSPRequest(X509Certificate certificate, TokenDTO tokenDTO) throws Exception;

    OCSPResp sendOCSPRequest(OCSPReq ocspReq, TokenDTO token) throws Exception;

    public boolean processOCSPResponse(OCSPReq ocspReq, OCSPResp ocspResp, TokenDTO token) throws Exception;

}

