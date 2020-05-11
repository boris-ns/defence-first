package rs.ac.uns.ftn.siemcentar.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Log")
public class Log {

    @Id
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
