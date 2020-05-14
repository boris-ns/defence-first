package rs.ac.uns.ftn.pkiservice.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateCertificateDTO {

    @NotNull(message = "County code is required")
    @Size(min = 2, max = 2, message = "County code length must be 2")
    private String countryCode;

    @NotNull(message = "State is required")
    @Size(min = 1, message = "State length must be higher or equal to 1")
    private String state;

    @NotNull(message = "Locality is required")
    @Size(min = 1, message = "Locality length must be higher or equal to 1")
    private String locality;

    @NotNull(message = "Organization is required")
    @Size(min = 1, message = "Organization length must be higher or equal to 1")
    private String organization;

    @NotNull(message = "Organizational unit is required")
    @Size(min = 1, message = "Organizational unit length must be higher or equal to 1")
    private String organizationalUnit;

    @NotNull(message = "Common name is required")
    @Size(min = 1, message = "Common name must be higher or equal to 1")
    private String commonName;

    @NotNull(message = "Issuer alias is required")
    @Size(min = 1, message = "State length must be higher or equal to 1")
    private String issuerAlias;

    public CreateCertificateDTO(String countryCode, String state, String locality, String organization, String organizationalUnit, String commonName, String issuerAlias) {
        this.countryCode = countryCode;
        this.state = state;
        this.locality = locality;
        this.organization = organization;
        this.organizationalUnit = organizationalUnit;
        this.commonName = commonName;
        this.issuerAlias = issuerAlias;
    }

    public CreateCertificateDTO() {
    }

    public String getIssuerAlias() {
        return issuerAlias;
    }

    public void setIssuerAlias(String issuerAlias) {
        this.issuerAlias = issuerAlias;
    }

    public String getCountryCode() { return countryCode; }

    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getLocality() { return locality; }

    public void setLocality(String locality) { this.locality = locality; }

    public String getOrganization() { return organization; }

    public void setOrganization(String organization) { this.organization = organization; }

    public String getOrganizationalUnit() { return organizationalUnit; }

    public void setOrganizationalUnit(String organizationalUnit) { this.organizationalUnit = organizationalUnit; }

    public String getCommonName() { return commonName; }

    public void setCommonName(String commonName) { this.commonName = commonName; }
}
