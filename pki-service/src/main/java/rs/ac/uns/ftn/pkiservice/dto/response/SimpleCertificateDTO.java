package rs.ac.uns.ftn.pkiservice.dto.response;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.Date;

public class SimpleCertificateDTO {

    private String subjectData;
    private String issuerData;
    private BigInteger serialNumber;
    private Date notBefore;
    private Date notAfter;
    private boolean revoked;

    public SimpleCertificateDTO() {
    }

    public SimpleCertificateDTO(String subjectData, String issuerData, BigInteger serialNumber,
                                Date notBefore, Date notAfter) {
        this.subjectData = subjectData;
        this.issuerData = issuerData;
        this.serialNumber = serialNumber;
        this.notBefore = notBefore;
        this.notAfter = notAfter;
    }

    public SimpleCertificateDTO(X509Certificate certificate, boolean revoked) {
        this.subjectData = certificate.getSubjectX500Principal().toString();
        this.issuerData = certificate.getIssuerX500Principal().toString();
        this.serialNumber = certificate.getSerialNumber();
        this.notBefore = certificate.getNotBefore();
        this.notAfter = certificate.getNotAfter();
        this.revoked = revoked;
    }

    public String getSubjectData() {
        return subjectData;
    }

    public void setSubjectData(String subjectData) {
        this.subjectData = subjectData;
    }

    public String getIssuerData() {
        return issuerData;
    }

    public void setIssuerData(String issuerData) {
        this.issuerData = issuerData;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Date notBefore) {
        this.notBefore = notBefore;
    }

    public Date getNotAfter() {
        return notAfter;
    }

    public void setNotAfter(Date notAfter) {
        this.notAfter = notAfter;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
