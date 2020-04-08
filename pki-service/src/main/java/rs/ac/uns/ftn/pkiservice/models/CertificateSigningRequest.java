package rs.ac.uns.ftn.pkiservice.models;

import rs.ac.uns.ftn.pkiservice.models.enums.CSRStatus;

import javax.persistence.*;

@Entity
@Table(name = "certificate_signing_requests")
public class CertificateSigningRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "csr", columnDefinition="TEXT", nullable = false)
    private String csr;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "issuer_name", nullable = false)
    private String issuerName;

    @Column(name = "status", nullable = false)
    @Enumerated
    private CSRStatus status;

    public CertificateSigningRequest() {
    }

    public CertificateSigningRequest(String csr, String subjectName, String issuerName) {
        this.csr = csr;
        this.subjectName = subjectName;
        this.issuerName = issuerName;
        this.status = CSRStatus.WAITING;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCsr() {
        return csr;
    }

    public void setCsr(String csr) {
        this.csr = csr;
    }

    public CSRStatus getStatus() {
        return status;
    }

    public void setStatus(CSRStatus status) {
        this.status = status;
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
}