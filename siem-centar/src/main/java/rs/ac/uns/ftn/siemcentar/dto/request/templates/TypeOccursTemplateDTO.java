package rs.ac.uns.ftn.siemcentar.dto.request.templates;

import rs.ac.uns.ftn.siemcentar.model.LogType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TypeOccursTemplateDTO {

    @NotNull(message = "Log type is required")
    private LogType type;

    @NotNull(message = "Number of occurs is required")
    @Min(1)
    private Integer occurs;

    @NotNull(message = "Alarm message is required")
    @NotBlank
    private String alarmMessage;

    @NotNull(message = "Time is required")
    @Min(1)
    private Integer time;

    public TypeOccursTemplateDTO() {
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public Integer getOccurs() {
        return occurs;
    }

    public void setOccurs(Integer occurs) {
        this.occurs = occurs;
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
