package rs.ac.uns.ftn.siemcentar.dto.response;

import java.util.List;

public class ReportsDTO {

    private List<AgentReportDTO> reports;

    public ReportsDTO() {
    }

    public ReportsDTO(List<AgentReportDTO> reports) {
        this.reports = reports;
    }

    public List<AgentReportDTO> getReports() {
        return reports;
    }

    public void setReports(List<AgentReportDTO> reports) {
        this.reports = reports;
    }
}
