package rs.ac.uns.ftn.pkiservice.dto.response;

import java.util.HashMap;

public class CertificateRequestDTO {

    private String subjectData;
    private HashMap<String, String> extensions;

    public CertificateRequestDTO() {
    }

    public CertificateRequestDTO(String subjectData, HashMap<String, String> extensions) {
        this.subjectData = subjectData;
        this.extensions = extensions;
    }

    public String getSubjectData() {
        return subjectData;
    }

    public void setSubjectData(String subjectData) {
        this.subjectData = subjectData;
    }

    public HashMap<String, String> getExtensions() {
        return extensions;
    }

    public void setExtensions(HashMap<String, String> extensions) {
        this.extensions = extensions;
    }
}
