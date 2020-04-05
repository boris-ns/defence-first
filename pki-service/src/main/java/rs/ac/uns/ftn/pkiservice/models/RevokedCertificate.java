package rs.ac.uns.ftn.pkiservice.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "revoked_certificates")
public class RevokedCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "date", nullable = false)
    private Date date;

    public RevokedCertificate() {
    }

    public RevokedCertificate(String serialNumber, Date date) {
        this.serialNumber = serialNumber;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
