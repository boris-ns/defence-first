package rs.ac.uns.ftn.siemcentar.model;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Alarm")
@Role(Role.Type.EVENT)
@Expires("30m")
public class Alarm {

    @Transient
    public static final String SEQUENCE_NAME = "alarm_sequence";

    @Id
    private Long id;
    private Date date;
    private String reason;
    private Long logId;

    public Alarm() {
    }

    public Alarm(Long id, Date date, String reason, Long logId) {
        this.id = id;
        this.date = date;
        this.reason = reason;
        this.logId = logId;
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

    public Long getLogId() { return logId; }

    public void setLogId(Long logId) { this.logId = logId; }
}
