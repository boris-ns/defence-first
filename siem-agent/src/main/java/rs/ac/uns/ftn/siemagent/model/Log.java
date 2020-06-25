package rs.ac.uns.ftn.siemagent.model;

import java.io.Serializable;
import java.util.Date;


public class Log implements Serializable {

    private Long id;
    private Date date;
    private LogType logType;
    private int severity;
    private String message;
    private String source;
    private String agent;

    public Log() {}

    public Log(Long id, Date date, LogType logType, int severity, String message, String source, String agent) {
        this.id = id;
        this.date = date;
        this.logType = logType;
        this.severity = severity;
        this.message = message;
        this.source = source;
        this.agent = agent;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", date=" + date +
                ", logType=" + logType +
                ", message='" + message + '\'' +
                ", source='" + source + '\'' +
                ", agent='" + agent + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public LogType getLogType() {
        return logType;
    }

    public String getMessage() {
        return message;
    }

    public String getSource() {
        return source;
    }

    public String getAgent() {
        return agent;
    }

    public int getSeverity() { return severity; }
}
