package rs.ac.uns.ftn.siemcentar.service;

import rs.ac.uns.ftn.siemcentar.dto.request.ReportRequestDTO;
import rs.ac.uns.ftn.siemcentar.dto.response.ReportsDTO;

public interface ReportsService {

    ReportsDTO createReports(ReportRequestDTO requestDto);
}
