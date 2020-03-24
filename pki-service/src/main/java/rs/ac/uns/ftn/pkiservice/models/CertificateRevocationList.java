package rs.ac.uns.ftn.pkiservice.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "CertificateRevocationList")
public class CertificateRevocationList {

    @Id
    private String id;
    private List<CertificateRevocationItem> revokedCertifications;

    public CertificateRevocationList() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CertificateRevocationItem> getRevokedCertifications() {
        return revokedCertifications;
    }

    public void setRevokedCertifications(List<CertificateRevocationItem> revokedCertifications) {
        this.revokedCertifications = revokedCertifications;
    }
}
