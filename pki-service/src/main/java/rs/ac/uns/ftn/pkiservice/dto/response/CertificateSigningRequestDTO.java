package rs.ac.uns.ftn.pkiservice.dto.response;

import rs.ac.uns.ftn.pkiservice.models.CertificateSigningRequest;
import rs.ac.uns.ftn.pkiservice.models.enums.CSRStatus;

public class CertificateSigningRequestDTO {

    private Long id;
    private String subjectName;
    private String issuerName;
    private CSRStatus status;

    public CertificateSigningRequestDTO() {
    }

    public CertificateSigningRequestDTO(CertificateSigningRequest csr) {
        this.id = csr.getId();
        this.subjectName = csr.getSubjectName();
        this.issuerName = csr.getIssuerName();
        this.status = csr.getStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public CSRStatus getStatus() { return status; }

    public void setStatus(CSRStatus status) { this.status = status; }
}
