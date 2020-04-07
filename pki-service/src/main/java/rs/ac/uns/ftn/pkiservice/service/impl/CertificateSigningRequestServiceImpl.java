package rs.ac.uns.ftn.pkiservice.service.impl;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
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
import rs.ac.uns.ftn.pkiservice.exception.exceptions.ApiRequestException;
import rs.ac.uns.ftn.pkiservice.exception.exceptions.ResourceNotFoundException;
import rs.ac.uns.ftn.pkiservice.models.CertificateSigningRequest;
import rs.ac.uns.ftn.pkiservice.models.enums.CSRStatus;
import rs.ac.uns.ftn.pkiservice.repository.CertificateSigningRequestRepository;
import rs.ac.uns.ftn.pkiservice.service.CertificateSigningRequestService;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CertificateSigningRequestServiceImpl implements CertificateSigningRequestService {

    @Autowired
    private CertificateSigningRequestRepository csrRepository;

    @Override
    public List<CertificateSigningRequest> findAllWaitingRequests() {
        return csrRepository.findByStatus(CSRStatus.WAITING);
    }

    @Override
    public void addRequest(String csr) throws PKCSException, IOException, OperatorCreationException {
        PEMParser pm = new PEMParser(new StringReader(csr));
        PKCS10CertificationRequest certReq = (PKCS10CertificationRequest) pm.readObject();
        ContentVerifierProvider prov = new JcaContentVerifierProviderBuilder().build(certReq.getSubjectPublicKeyInfo());

        if (!certReq.isSignatureValid(prov)) {
            throw new ApiRequestException("CSR is not valid");
        }

        Map<String, String> attributes = this.parseCsrAttributes(certReq);

        CertificateSigningRequest request = new CertificateSigningRequest(csr, certReq.getSubject().toString(), attributes.get("issuerId"));
        csrRepository.save(request);
    }

    private Map<String, String> parseCsrAttributes(PKCS10CertificationRequest csr) {
        Map<String, String> result = new HashMap<>();

        String issuerId = null;

        Attribute[] attributes = csr.getAttributes(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest);
        for (Attribute attribute : attributes) {
            for (ASN1Encodable value : attribute.getAttributeValues()) {
                DEROctetString oo = (DEROctetString) ((DERTaggedObject) ((DERSequence)value).getObjectAt(0)).getObject();
                issuerId = new String(oo.getOctets());
            }
        }

        // @TODO: Dodati ostale atribute u mapu
        result.put("issuerId", issuerId);

        return result;
    }

    @Override
    public void changeStatus(Long id, CSRStatus toStatus) {
        CertificateSigningRequest csr = csrRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("CSR with id " + id + " doesn't exist"));

        if (!csr.getStatus().equals(CSRStatus.WAITING)) {
            throw new ApiRequestException("You can't change status for this CSR");
        }

        csr.setStatus(toStatus);
        csrRepository.save(csr);

        if (toStatus.equals(CSRStatus.APPROVED)) {
            // @TODO: Kreirati sertifikat
        }
    }
}
