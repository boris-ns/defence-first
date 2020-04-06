package rs.ac.uns.ftn.pkiservice.dto.response;

import java.math.BigInteger;
import java.util.Date;

public class SimpleCertificateDTO {

    private String subjectData;
    private String issuerData;
    private BigInteger serialNumber;
    private Date notBefore;
    private Date notAfter;

    public SimpleCertificateDTO() {
    }

    public SimpleCertificateDTO(String subjectData, String issuerData, BigInteger serialNumber, Date notBefore, Date notAfter) {
        this.subjectData = subjectData;
        this.issuerData = issuerData;
        this.serialNumber = serialNumber;
        this.notBefore = notBefore;
        this.notAfter = notAfter;
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
}
