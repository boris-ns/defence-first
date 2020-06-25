package rs.ac.uns.ftn.siemcentar.model.template;

import rs.ac.uns.ftn.siemcentar.model.LogType;

public class TypeMessageTemplate {

    private LogType type;
    private String messageRegex;
    private String alarmMessage;

    public TypeMessageTemplate() {
    }

    public TypeMessageTemplate(LogType type, String messageRegex, String alarmMessage) {
        this.type = type;
        this.messageRegex = messageRegex;
        this.alarmMessage = alarmMessage;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public String getMessageRegex() {
        return messageRegex;
    }

    public void setMessageRegex(String messageRegex) {
        this.messageRegex = messageRegex;
    }

    public String getAlarmMessage() {
        return alarmMessage;
    }

    public void setAlarmMessage(String alarmMessage) {
        this.alarmMessage = alarmMessage;
    }
}
