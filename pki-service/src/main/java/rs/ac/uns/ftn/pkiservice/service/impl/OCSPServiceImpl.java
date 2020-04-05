package rs.ac.uns.ftn.pkiservice.service.impl;

import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.ocsp.*;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.pkiservice.exception.exceptions.ResourceNotFoundException;
import rs.ac.uns.ftn.pkiservice.models.RevokedCertificate;
import rs.ac.uns.ftn.pkiservice.repository.KeyStoreRepository;
import rs.ac.uns.ftn.pkiservice.repository.RevokedCertificateRepository;
import rs.ac.uns.ftn.pkiservice.service.CertificateService;
import rs.ac.uns.ftn.pkiservice.service.OCSPService;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;

@Service
public class OCSPServiceImpl implements OCSPService {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private KeyStoreRepository keyStoreRepository;

    @Autowired
    private RevokedCertificateRepository revokedCertificateRepository;

    /*
    *
    * params:
    *   issuerCert: potreban je sertifikat roditelja
    *   serialNumber: serijski broj sertifikata za kojeg proveravamo da li je Revoked ili nije
    * */
    @Override
    public OCSPReq generateOCSPRequest(X509Certificate issuerCert, BigInteger serialNumber)
            throws OCSPException, OperatorCreationException, CertificateEncodingException, IOException {

        BcDigestCalculatorProvider util = new BcDigestCalculatorProvider();

        // Generate the id for the certificate we are looking for
        CertificateID id = new CertificateID(util.get(  CertificateID.HASH_SHA1),
                new X509CertificateHolder(issuerCert.getEncoded()), serialNumber);

        OCSPReqBuilder ocspGen = new OCSPReqBuilder();
        ocspGen.addRequest(id);

        // nonce je vrednost koju onaj koji salje zahtev salje kako bi se stitili od replay napada.
        BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
        Extension ext = new Extension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce, true, new DEROctetString(nonce.toByteArray()));
        ocspGen.setRequestExtensions(new Extensions(new Extension[] { ext }));

        return ocspGen.build();
    }


    /*
    *   responderKey: privatni kljuc OCSP Servera koji odgovara --> da to bude KLJUC CA root-a?
    *   PublicKey: ovo bi trebalo da je publicKey sertifikata za koji proveravamo..
    *   CertificateID revokedID ---> treba kad povlacimo sertifikate da imamo ovakve u bazi
    * */

    @Override
    public OCSPResp generateOCSPResponse(OCSPReq request, PrivateKey responderKey, PublicKey pubKey)
            throws OCSPException, OperatorCreationException {

        BcDigestCalculatorProvider util = new BcDigestCalculatorProvider();

        // ako znam kako sam ga zbudzio
        BasicOCSPRespBuilder respBuilder = new BasicOCSPRespBuilder(
                SubjectPublicKeyInfo.getInstance(pubKey.getEncoded()),
                util.get(CertificateID.HASH_SHA1));


        Extensions extensions = null;
        // vracamo samo onu nonce i tjt.
        Extension nonce_ext = request.getExtension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce);
        if (nonce_ext != null) {
                extensions = new Extensions(new Extension[]{ nonce_ext});
        }

        Req[] requests = request.getRequestList();
        for (Req req : requests) {
            BigInteger sn = req.getCertID().getSerialNumber();
            X509Certificate cert = (X509Certificate) this.certificateService.getCertificateByAlias(sn.toString());

            if (cert == null) {
                respBuilder.addResponse(req.getCertID(), new UnknownStatus());
            } else {
                try {
                    // @TODO: proverava samo datume a nzm sta bi moglo jos
                    cert.checkValidity();
                    respBuilder.addResponse(req.getCertID(), CertificateStatus.GOOD);
                }
                catch (Exception e) {
                    respBuilder.addResponse(req.getCertID(), new RevokedStatus(new Date(), CRLReason.superseded));
                }
            }
        }

        BasicOCSPResp resp = respBuilder.build(
                new JcaContentSignerBuilder("SHA256withRSA").build(responderKey),
                null, new Date());

        OCSPRespBuilder builder = new OCSPRespBuilder();
        return builder.build(OCSPRespBuilder.SUCCESSFUL, resp);
    }

    @Override
    public boolean addCertificateToOCSP(String serialNumber) {
        if (keyStoreRepository.readCertificate(serialNumber) == null) {
            throw new ResourceNotFoundException("Certificate does not exist");
        }

        RevokedCertificate revokedCertificate = new RevokedCertificate(serialNumber, new Date());
        revokedCertificateRepository.save(revokedCertificate);

        return true;
    }

    @Override
    public boolean isCertificateRevoked(String serialNumber) {
        return revokedCertificateRepository.findBySerialNumber(serialNumber).isPresent();
    }
}
