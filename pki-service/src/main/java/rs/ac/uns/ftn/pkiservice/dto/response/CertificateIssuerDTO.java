package rs.ac.uns.ftn.pkiservice.dto.response;

public class CertificateIssuerDTO {

    private String name;
    private String alias;

    public CertificateIssuerDTO(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public CertificateIssuerDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
