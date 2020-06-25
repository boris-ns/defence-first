package rs.ac.uns.ftn.siemcentar.dto.response;

import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.model.LogType;

import java.util.Date;

public class LogDTO {

    private Long id;
    private Date date;
    private LogType logType;
    private int severity;
    private String message;
    private String source;
    private String agent;

    public LogDTO() {
    }

    public LogDTO(Log log) {
        this.id = log.getId();
        this.date = log.getDate();
        this.severity = log.getSeverity();
        this.logType = log.getLogType();
        this.message = log.getMessage();
        this.source = log.getSource();
        this.agent = log.getAgent();
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

    public int getSeverity() { return severity; }

    public void setSeverity(int severity) { this.severity = severity; }
}
