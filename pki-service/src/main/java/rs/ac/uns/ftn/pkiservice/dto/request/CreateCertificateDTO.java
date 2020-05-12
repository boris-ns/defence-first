package rs.ac.uns.ftn.pkiservice.dto.request;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

public class CreateCertificateDTO {

    @NotNull(message = "Subject name is required")
    private HashMap<String, String> subjectName;

    @NotNull(message = "Issuer alias is required")
    private String issuerAlias;

    public CreateCertificateDTO(HashMap<String, String> subjectName, String issuerAlias) {
        this.subjectName = subjectName;
        this.issuerAlias = issuerAlias;
    }

    public CreateCertificateDTO() {
    }

    public HashMap<String, String> getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(HashMap<String, String> subjectName) {
        this.subjectName = subjectName;
    }

    public String getIssuerAlias() {
        return issuerAlias;
    }

    public void setIssuerAlias(String issuerAlias) {
        this.issuerAlias = issuerAlias;
    }
}
