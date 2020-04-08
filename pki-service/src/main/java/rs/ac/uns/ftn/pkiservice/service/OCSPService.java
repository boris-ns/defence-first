package rs.ac.uns.ftn.pkiservice.service;

import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.bouncycastle.operator.OperatorCreationException;

import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

public interface OCSPService {
    
    OCSPResp generateOCSPResponse (OCSPReq request)
            throws OCSPException, OperatorCreationException;

    boolean addCertificateToOCSP(String serialNumber);

    boolean isCertificateRevoked(String serialNumber);
}
