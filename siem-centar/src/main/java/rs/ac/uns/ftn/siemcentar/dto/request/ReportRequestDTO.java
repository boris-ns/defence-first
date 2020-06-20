package rs.ac.uns.ftn.siemcentar.dto.request;

import javax.validation.constraints.NotNull;

public class ReportRequestDTO {

    @NotNull(message = "Start datetime is required")
    private int[] startDate;

    @NotNull(message = "End datetime is required")
    private int[] endDate;

    private boolean showAll;

    public ReportRequestDTO() {
    }

    public boolean isShowAll() {
        return showAll;
    }

    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }

    public int[] getStartDate() {
        return startDate;
    }

    public void setStartDate(int[] startDate) {
        this.startDate = startDate;
    }

    public int[] getEndDate() {
        return endDate;
    }

    public void setEndDate(int[] endDate) {
        this.endDate = endDate;
    }
}
