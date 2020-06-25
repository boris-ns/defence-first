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
    private String agent;
    private String source;
    private Long logId;
    private AlarmType type;

    public Alarm() {
    }

    public Alarm(Long id, Date date, String reason, Long logId, String agent, String source, AlarmType type) {
        this.id = id;
        this.date = date;
        this.reason = reason;
        this.logId = logId;
        this.agent = agent;
        this.source = source;
        this.type = type;
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

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getLogId() { return logId; }

    public void setLogId(Long logId) { this.logId = logId; }

    public AlarmType getType() {
        return type;
    }

    public void setType(AlarmType type) {
        this.type = type;
    }
}
