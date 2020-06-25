package rs.ac.uns.ftn.siemcentar.dto.response;

import rs.ac.uns.ftn.siemcentar.model.Alarm;
import rs.ac.uns.ftn.siemcentar.model.AlarmType;

import java.util.Date;

public class AlarmDTO {

    private Long id;
    private Date date;
    private AlarmType alarmType;
    private String reason;

    public AlarmDTO() {
    }

    public AlarmDTO(Alarm alarm) {
        this.id = alarm.getId();
        this.date = alarm.getDate();
        this.alarmType = alarm.getType();
        this.reason = alarm.getReason();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AlarmType getAlarmType() { return alarmType; }

    public void setAlarmType(AlarmType alarmType) { this.alarmType = alarmType; }
}
