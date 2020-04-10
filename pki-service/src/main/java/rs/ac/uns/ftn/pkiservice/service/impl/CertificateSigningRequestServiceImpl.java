package rs.ac.uns.ftn.pkiservice.service.impl;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.exception.exceptions.ApiRequestException;
import rs.ac.uns.ftn.pkiservice.exception.exceptions.ResourceNotFoundException;
import rs.ac.uns.ftn.pkiservice.models.CertificateSigningRequest;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;
import rs.ac.uns.ftn.pkiservice.models.enums.CSRStatus;
import rs.ac.uns.ftn.pkiservice.repository.CertificateSigningRequestRepository;
import rs.ac.uns.ftn.pkiservice.service.CertificateGeneratorService;
import rs.ac.uns.ftn.pkiservice.service.CertificateService;
import rs.ac.uns.ftn.pkiservice.service.CertificateSigningRequestService;

import java.io.IOException;
import java.io.StringReader;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CertificateSigningRequestServiceImpl implements CertificateSigningRequestService {

    @Autowired
    private CertificateSigningRequestRepository csrRepository;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CertificateGeneratorService certificateGeneratorService;


    @Override
    public List<CertificateSigningRequest> findAllWaitingRequests() {
        return csrRepository.findByStatus(CSRStatus.WAITING);
    }

    @Override
    public PKCS10CertificationRequest isValidSigned(String csr, Boolean renew) throws PKCSException, IOException, OperatorCreationException{
        PEMParser pm = new PEMParser(new StringReader(csr));
        PKCS10CertificationRequest certReq = (PKCS10CertificationRequest) pm.readObject();

        Map<String, String> attributes = this.parseCsrAttributes(certReq);

        ContentVerifierProvider prov = null;
        if (!renew) {
            prov = new JcaContentVerifierProviderBuilder().build(certReq.getSubjectPublicKeyInfo());
        }else {
            //@TODO: Pronadji stari PublicKey i sa njim proveri potpis.. NECEEEE
//            String serialNumber = attributes.get("certSerialNumber");
//            X509Certificate certificate = certificateService.findCertificateByAlias(serialNumber);
//            prov = new JcaContentVerifierProviderBuilder().build(certificate.getPublicKey());
        }

//        if (!certReq.isSignatureValid(prov)) {
//            throw new ApiRequestException("CSR renewal is not valid");
//        }
        return certReq;
    }

    @Override
    public void addRequest(String csr) throws PKCSException, IOException, OperatorCreationException {
        PKCS10CertificationRequest certReq = isValidSigned(csr, false);
        Map<String, String> attributes = this.parseCsrAttributes(certReq);
        CertificateSigningRequest request = new CertificateSigningRequest(csr, certReq.getSubject().toString(), attributes.get("issuerId"));
        csrRepository.save(request);
    }

    @Override
    public void addRenewRequest(String csr) throws PKCSException, IOException, OperatorCreationException {
        PKCS10CertificationRequest certReq = isValidSigned(csr, true);
        Map<String, String> attributes = this.parseCsrAttributes(certReq);
        CertificateSigningRequest request = new CertificateSigningRequest(csr, certReq.getSubject().toString(), attributes.get("issuerId"));
        request.setStatus(CSRStatus.WAITING_RENEWAL);
        csrRepository.save(request);
    }

    private Map<String, String> parseCsrAttributes(PKCS10CertificationRequest csr) throws IOException {
        Map<String, String> result = new HashMap<>();

        String attrVal = null;
        //@TODO: BOLJE CITATI KLJUCEVE
        String[] attNames = {"issuerId", "certSerialNumber"};

        Attribute[] attributes = csr.getAttributes(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest);
        for (Attribute attribute : attributes) {
            int i = 0;
            System.out.println(attribute.getAttrType());
            for (ASN1Encodable values : attribute.getAttributeValues()) {
                for(ASN1Encodable value : (DERSequence)values) {
                    DEROctetString oo = (DEROctetString) ((DERTaggedObject)value).getObject();
                    attrVal = new String(oo.getOctets());
                    result.put(attNames[i], attrVal);
                    i++;
                }
            }
        }
        return result;
    }

    //@TODO: ARIIVITI STARI i A SACUVATI OVAJ NOVI!!!!
    @Override
    public X509Certificate saveCertificateRequest(String csr, Boolean renewal) throws Exception {

        PKCS10CertificationRequest certReq = isValidSigned(csr, renewal);
        Map<String, String> attributes = parseCsrAttributes(certReq);

        //@TODO  UZ POMOC OVOG NACI STARI SERTIFIKAT I STAVITI GA U ARHIVU.
        String serialNumber = attributes.get("certSerialNumber");

        IssuerData issuerData= certificateService.findIssuerByAlias(attributes.get("issuerId"));
        PublicKey pk = certificateGeneratorService.toPublicKey(certReq.getSubjectPublicKeyInfo());
        SubjectData subData = certificateGeneratorService.generateSubjectData(pk, certReq.getSubject(), Constants.CERT_TYPE.LEAF_CERT);
        X509Certificate newCert = certificateGeneratorService.generateCertificate(subData,issuerData, Constants.CERT_TYPE.LEAF_CERT);
        Certificate[] certificates = certificateService.createChain(attributes.get("issuerId"), newCert);
        // stavljeno da se cuva sa pk od issuera u sustini taj kljuc nam ne sluzi ni za st, ali mora neki da se prosledi
        certificateService.writeCertificateToKeyStore(newCert.getSerialNumber().toString(), certificates,
                issuerData.getPrivateKey());
        return newCert;
    }

    @Override
    public void changeStatus(Long id, CSRStatus toStatus) throws Exception {
        CertificateSigningRequest csr = csrRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("CSR with id " + id + " doesn't exist"));

        if (!csr.getStatus().equals(CSRStatus.WAITING) || !csr.getStatus().equals(CSRStatus.WAITING_RENEWAL)) {
            throw new ApiRequestException("You can't change status for this CSR");
        }

        csr.setStatus(toStatus);
        csrRepository.save(csr);

        if (toStatus.equals(CSRStatus.APPROVED)) {
            saveCertificateRequest(csr.getCsr(), csr.getStatus() == CSRStatus.WAITING_RENEWAL);
        }
    }
}
