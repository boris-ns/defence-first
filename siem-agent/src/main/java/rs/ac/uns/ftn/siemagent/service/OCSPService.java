package rs.ac.uns.ftn.siemagent.service;

import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.operator.OperatorCreationException;

import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

public interface OCSPService {

    OCSPReq generateOCSPRequest(X509Certificate certificate) throws Exception;

    String sendOCSPRequest(OCSPReq ocspReq) throws Exception;
}
