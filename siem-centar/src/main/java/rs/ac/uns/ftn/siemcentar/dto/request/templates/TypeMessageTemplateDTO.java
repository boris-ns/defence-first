package rs.ac.uns.ftn.siemcentar.dto.request.templates;

import rs.ac.uns.ftn.siemcentar.model.LogType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TypeMessageTemplateDTO {

    @NotNull(message = "Log type is required")
    private LogType type;

    @NotNull(message = "Message regex is required")
    @NotBlank
    private String messageRegex;

    @NotNull(message = "Alarm message is required")
    @NotBlank
    private String alarmMessage;

    public TypeMessageTemplateDTO() {
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
