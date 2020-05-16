package rs.ac.uns.ftn.siemcentar.dto.response;

import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.model.LogType;

public class LogDTO {

    private Long id;
    private String date;
    private LogType logType;
    private String message;
    private String source;

    public LogDTO() {
    }

    public LogDTO(Log log) {
        this.id = log.getId();
        this.date = log.getDate().toString();
        this.logType = log.getLogType();
        this.message = log.getMessage();
        this.source = log.getSource();
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
}
