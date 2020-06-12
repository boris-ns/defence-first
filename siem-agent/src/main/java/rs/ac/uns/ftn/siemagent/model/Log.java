package rs.ac.uns.ftn.siemagent.model;

import java.io.Serializable;
import java.util.Date;


public class Log implements Serializable {

    private Long id;
    private Date date;
    private LogType logType;
    private String message;
    private String source;

    public Log() {}

    public Log(Long id, Date date, LogType logType, String message, String source) {
        this.id = id;
        this.date = date;
        this.logType = logType;
        this.message = message;
        this.source = source;
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
}
