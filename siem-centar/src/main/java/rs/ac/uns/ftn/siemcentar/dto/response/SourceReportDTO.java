package rs.ac.uns.ftn.siemcentar.dto.response;

public class SourceReportDTO {

    private String source;
    private Long numOfLogs;
    private Long numOfAlarms;

    public SourceReportDTO() {
    }

    public SourceReportDTO(String source, Long numOfLogs, Long numOfAlarms) {
        this.source = source;
        this.numOfLogs = numOfLogs;
        this.numOfAlarms = numOfAlarms;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getNumOfLogs() {
        return numOfLogs;
    }

    public void setNumOfLogs(Long numOfLogs) {
        this.numOfLogs = numOfLogs;
    }

    public Long getNumOfAlarms() {
        return numOfAlarms;
    }

    public void setNumOfAlarms(Long numOfAlarms) {
        this.numOfAlarms = numOfAlarms;
    }
}
