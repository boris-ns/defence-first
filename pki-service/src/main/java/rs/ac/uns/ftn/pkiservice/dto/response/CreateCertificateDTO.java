package rs.ac.uns.ftn.pkiservice.dto.response;

public class CreateCertificateDTO {

    private String subjectName;
    private String issuerAlias;

    public CreateCertificateDTO(String subjectName, String issuerAlias) {
        this.subjectName = subjectName;
        this.issuerAlias = issuerAlias;
    }

    public CreateCertificateDTO() {
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getIssuerAlias() {
        return issuerAlias;
    }

    public void setIssuerAlias(String issuerAlias) {
        this.issuerAlias = issuerAlias;
    }
}
