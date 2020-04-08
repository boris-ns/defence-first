package rs.ac.uns.ftn.pkiservice.dto.response;

import rs.ac.uns.ftn.pkiservice.models.CertificateSigningRequest;

public class CertificateSigningRequestDTO {

    private Long id;
    private String subjectName;
    private String issuerName;

    public CertificateSigningRequestDTO() {
    }

    public CertificateSigningRequestDTO(CertificateSigningRequest csr) {
        this.id = csr.getId();
        this.subjectName = csr.getSubjectName();
        this.issuerName = csr.getIssuerName();
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
}
