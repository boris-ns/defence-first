package rs.ac.uns.ftn.siemcentar.dto.response;

import java.util.List;

public class AgentReportDTO {

    private String agent;
    private Long numOfLogs;
    private Long numOfAlarms;
    private List<SourceReportDTO> sources;

    public AgentReportDTO() {
    }

    public AgentReportDTO(String agent, Long numOfLogs, Long numOfAlarms, List<SourceReportDTO> sources) {
        this.agent = agent;
        this.numOfLogs = numOfLogs;
        this.numOfAlarms = numOfAlarms;
        this.sources = sources;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
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

    public List<SourceReportDTO> getSources() {
        return sources;
    }

    public void setSources(List<SourceReportDTO> sources) {
        this.sources = sources;
    }
}
