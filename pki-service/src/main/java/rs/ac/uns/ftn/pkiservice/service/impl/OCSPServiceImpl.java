package rs.ac.uns.ftn.pkiservice.service.impl;

import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.ocsp.*;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.pkiservice.exception.exceptions.ApiRequestException;
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
    *   responderKey: privatni kljuc OCSP Servera koji odgovara --> da to bude KLJUC CA root-a?
    *       ---> koristi se da se POTPISE ODGOVOR...
    *   PublicKey: ovo bi trebalo da je publicKey sertifikata za koji proveravamo..
    *   CertificateID revokedID ---> treba kad povlacimo sertifikate da imamo ovakve u bazi
    * */

    /*
    * da po nekoj ektenziji ili po name pronadjem koji je request i njegov lanaca da dobavimo
    *
    * onda se proveri potpis requesta, na osnovu requestorName-a..
    *
    * */

    // @TODO: Da li se ovo uopste ovako radi ?

    @Override
    public OCSPResp generateOCSPResponse(OCSPReq request)
            throws OCSPException, OperatorCreationException {

        String requestorCerticateSerialNumber = null;
        String requestorName = request.getRequestorName().toString();
        for(String reqParam: requestorName.split(",")) {
            if(reqParam.contains("UniqueIdentifier")) {
                requestorCerticateSerialNumber = reqParam.split("=")[1];
            }
        }

        X509Certificate requestMaker = (X509Certificate)
                keyStoreRepository.readCertificate(requestorCerticateSerialNumber);
        ContentVerifierProvider prov = new JcaContentVerifierProviderBuilder().build(requestMaker.getPublicKey());
        if (!request.isSignatureValid(prov)) {
            throw  new ApiRequestException("bed request signature ");
        }


        System.out.println(requestMaker.getSerialNumber());
        System.out.println(requestMaker.getPublicKey());

        return null;
        


//        ====> DA LI SU OBA KLJUCA OD CA-a??
//        PrivateKey responderKey = null;
//        PublicKey pubKey = null;
//
//        BcDigestCalculatorProvider util = new BcDigestCalculatorProvider();
//
//        // ako znam kako sam ga zbudzio
//        BasicOCSPRespBuilder respBuilder = new BasicOCSPRespBuilder(
//                SubjectPublicKeyInfo.getInstance(pubKey.getEncoded()),
//                util.get(CertificateID.HASH_SHA1));
//
//
//        Extensions extensions = null;
//        // vracamo samo onu nonce i tjt.
//        Extension nonce_ext = request.getExtension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce);
//        if (nonce_ext != null) {
//                extensions = new Extensions(new Extension[]{ nonce_ext});
//        }
//
//        Req[] requests = request.getRequestList();
//        for (Req req : requests) {
//            BigInteger sn = req.getCertID().getSerialNumber();
//            X509Certificate cert = (X509Certificate) keyStoreRepository.readCertificate(sn.toString());
//
//            if (cert == null) {
//                respBuilder.addResponse(req.getCertID(), new UnknownStatus());
//            } else {
//                try {
//                    // @TODO: proverava samo datume a nzm sta bi moglo jos
//                    // @TODO: Dodati proveru da li je sertifikat revoked
//
//                    cert.checkValidity();
//                    respBuilder.addResponse(req.getCertID(), CertificateStatus.GOOD);
//                }
//                catch (Exception e) {
//                    respBuilder.addResponse(req.getCertID(), new RevokedStatus(new Date(), CRLReason.superseded));
//                }
//            }
//        }
//
//        BasicOCSPResp resp = respBuilder.build(
//                new JcaContentSignerBuilder("SHA256withRSA").build(responderKey),
//                null, new Date());
//
//        OCSPRespBuilder builder = new OCSPRespBuilder();
//        return builder.build(OCSPRespBuilder.SUCCESSFUL, resp);
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
