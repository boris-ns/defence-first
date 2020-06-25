package rs.ac.uns.ftn.siemcentar.model.template;

import rs.ac.uns.ftn.siemcentar.model.LogType;

public class TypeOccursTemplate {

    private LogType type;
    private Integer count;
    private String alarmMessage;
    private Integer time;

    public TypeOccursTemplate() {
    }

    public TypeOccursTemplate(LogType type, Integer count, String alarmMessage, Integer time) {
        this.type = type;
        this.count = count;
        this.alarmMessage = alarmMessage;
        this.time = time;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getAlarmMessage() {
        return alarmMessage;
    }

    public void setAlarmMessage(String alarmMessage) {
        this.alarmMessage = alarmMessage;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
