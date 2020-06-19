package rs.ac.uns.ftn.siemcentar.dto.request;

import javax.validation.constraints.NotNull;

public class ReportRequestDTO {

    @NotNull(message = "Start datetime is required")
    private Long startDateTime;

    @NotNull(message = "End datetime is required")
    private Long endDateTime;

    public ReportRequestDTO() {
    }

    public Long getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Long startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Long getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Long endDateTime) {
        this.endDateTime = endDateTime;
    }
}
