package rs.ac.uns.ftn.pkiservice.service;

import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.bouncycastle.operator.OperatorCreationException;

import java.security.*;

public interface OCSPService {
    
    OCSPResp generateOCSPResponse (OCSPReq request)
            throws OCSPException, OperatorCreationException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException;

    boolean addCertificateToOCSP(String serialNumber);

    boolean isCertificateRevoked(String serialNumber);
}
