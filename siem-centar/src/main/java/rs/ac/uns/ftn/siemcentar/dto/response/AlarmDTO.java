package rs.ac.uns.ftn.siemcentar.dto.response;

import rs.ac.uns.ftn.siemcentar.model.Alarm;

public class AlarmDTO {

    private Long id;
    private String date;
    private String reason;

    public AlarmDTO() {
    }

    public AlarmDTO(Alarm alarm) {
        this.id = alarm.getId();
        this.date = alarm.getDate().toString();
        this.reason = alarm.getReason();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
