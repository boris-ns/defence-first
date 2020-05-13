package rs.ac.uns.ftn.siemagent.model;

import java.io.Serializable;
import java.util.Date;


public class Log implements Serializable {

    public Long id;
    public Date date;
    public LogType logType;
    public String message;
    public String source;

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
}
