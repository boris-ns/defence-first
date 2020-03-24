package rs.ac.uns.ftn.pkiservice.models;

import java.util.Date;

public class CertificateRevocationItem {

    private String certificationId;
    private Date revocationDate;

    public CertificateRevocationItem() {
    }

    public CertificateRevocationItem(String certificationId, Date revocationDate) {
        this.certificationId = certificationId;
        this.revocationDate = revocationDate;
    }

    public String getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(String certificationId) {
        this.certificationId = certificationId;
    }

    public Date getRevocationDate() {
        return revocationDate;
    }

    public void setRevocationDate(Date revocationDate) {
        this.revocationDate = revocationDate;
    }
}
