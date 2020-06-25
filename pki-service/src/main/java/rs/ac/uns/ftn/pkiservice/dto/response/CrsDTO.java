package rs.ac.uns.ftn.pkiservice.dto.response;

import rs.ac.uns.ftn.pkiservice.models.CertificateSigningRequest;
import rs.ac.uns.ftn.pkiservice.models.enums.CSRStatus;

public class CrsDTO {

    private CSRStatus status;
    private String serialNumber;

    public CrsDTO(CertificateSigningRequest certificateSigningRequest) {
        this.status = certificateSigningRequest.getStatus();
        this.serialNumber = certificateSigningRequest.getSerialNumber();
    }

    public CSRStatus getStatus() { return status; }

    public void setStatus(CSRStatus status) { this.status = status; }

    public String  getSerialNumber() { return serialNumber; }

    public void setSerialNumber(String  serialNumber) { this.serialNumber = serialNumber; }
}
