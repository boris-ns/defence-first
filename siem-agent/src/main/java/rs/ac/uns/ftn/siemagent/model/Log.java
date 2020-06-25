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

    private boolean processed;
    private String signature;
    private String ip;

    public Log() {this.processed =false;}

    public Log(Long id, Date date, LogType logType, int severity, String message, String source, String agent, String ip) {
        this.id = id;
        this.date = date;
        this.logType = logType;
        this.severity = severity;
        this.message = message;
        this.source = source;
        this.agent = agent;
        this.processed =false;
        this.ip = ip;
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
                    ", severity='" + severity + '\''+
                    ", ip='" + ip + '\'' +
                    ", processed='" + processed + '\'' +
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public boolean isProcessed() {
        return processed;
    }
    public String getIp() {
        return ip;
    }
    public int getSeverity() { return severity; }
}
