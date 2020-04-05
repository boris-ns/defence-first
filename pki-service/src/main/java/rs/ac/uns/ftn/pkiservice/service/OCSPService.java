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

    // @TODO prebaciti ovu metodu na SCIEM Agente jer ce oni da prave zahteve i na SCIEM centar
    public OCSPReq generateOCSPRequest(X509Certificate issuerCert, BigInteger serialNumber) throws OCSPException, OperatorCreationException, CertificateEncodingException, IOException;

    public OCSPResp generateOCSPResponse
            (OCSPReq request, PrivateKey responderKey, PublicKey pubKey)
            throws OCSPException, OperatorCreationException;

    public Boolean addCertficateToOCSP(BigInteger serialNumber);

}
