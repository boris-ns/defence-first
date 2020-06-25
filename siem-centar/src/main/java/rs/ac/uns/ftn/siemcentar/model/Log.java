package rs.ac.uns.ftn.siemcentar.model;

import org.kie.api.definition.type.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "Log")
@Role(Role.Type.EVENT)
public class Log implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "log_sequence";

    @Id
    private Long id;
    private Date date;
    private LogType logType;
    private String message;
    private String source;
    private String agent;
    private String ip;
    private Boolean ipBlacklisted;

    @Transient
    private Boolean processed;

    public Log() {
        this.processed = false;
    }

    public Log(Long id, Date date, LogType logType, String message, String source, String agent) {
        this.id = id;
        this.date = date;
        this.logType = logType;
        this.message = message;
        this.source = source;
        this.agent = agent;
        this.processed = false;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", date=" + date +
                ", logType=" + logType +
                ", message='" + message + '\'' +
                ", source='" + source + '\'' +
                '}';
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

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAgent() { return agent; }

    public void setAgent(String agent) { this.agent = agent; }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean getIpBlacklisted() {
        return ipBlacklisted;
    }

    public void setIpBlacklisted(Boolean ipBlacklisted) {
        this.ipBlacklisted = ipBlacklisted;
    }
}
