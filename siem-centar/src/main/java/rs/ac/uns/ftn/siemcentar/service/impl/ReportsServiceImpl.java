package rs.ac.uns.ftn.siemcentar.service.impl;

import com.mongodb.client.MongoCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemcentar.dto.request.ReportRequestDTO;
import rs.ac.uns.ftn.siemcentar.dto.response.AgentReportDTO;
import rs.ac.uns.ftn.siemcentar.dto.response.ReportsDTO;
import rs.ac.uns.ftn.siemcentar.dto.response.SourceReportDTO;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.repository.AlarmRepository;
import rs.ac.uns.ftn.siemcentar.repository.LogRepository;
import rs.ac.uns.ftn.siemcentar.service.ReportsService;
import rs.ac.uns.ftn.siemcentar.utils.DateUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportsServiceImpl implements ReportsService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ReportsDTO createReports(ReportRequestDTO requestDto) {
        Date startDate = null;
        Date endDate = null;

        if (!requestDto.isShowAll()) {
            startDate = DateUtils.dateFrom(requestDto.getStartDate());
            endDate = DateUtils.dateFrom(requestDto.getEndDate());
        }

        List<String> agents = this.getAgents();
        List<AgentReportDTO> agentReports = new ArrayList<>();

        for (String agent : agents) {
            Long agentNumOfLogs = 0L;
            Long agentNumOfAlarms = 0L;

            if (requestDto.isShowAll()) {
                agentNumOfLogs = logRepository.countLogsByAgentName(agent);
                agentNumOfAlarms = alarmRepository.countByAgentName(agent);
            } else {
                agentNumOfLogs = logRepository.countLogsByAgentNameAndDates(agent, startDate, endDate);
                agentNumOfAlarms = alarmRepository.countByAgentNameAndDates(agent, startDate, endDate);
            }

            List<SourceReportDTO> sourcesReports = new ArrayList<>();
            List<String> sources = this.getSourcesForAgent(agent);

            for (String source : sources) {
                Long sourceNumOfLogs = 0L;
                Long sourceNumOfAlarms = 0L;

                if (requestDto.isShowAll()) {
                    sourceNumOfLogs = logRepository.countLogsBySourceAndAgent(source, agent);
                    sourceNumOfAlarms = alarmRepository.countBySourceAndAgent(source, agent);
                } else {
                    sourceNumOfLogs = logRepository.countLogsBySourceAndAgentAndDates(source, agent, startDate, endDate);
                    sourceNumOfAlarms = alarmRepository.countBySourceAndAgentAndDates(source, agent, startDate, endDate);
                }

                SourceReportDTO sourceReportDTO = new SourceReportDTO(source, sourceNumOfLogs, sourceNumOfAlarms);
                sourcesReports.add(sourceReportDTO);
            }

            AgentReportDTO agentReportDTO = new AgentReportDTO(agent, agentNumOfLogs, agentNumOfAlarms, sourcesReports);
            agentReports.add(agentReportDTO);
        }

        return new ReportsDTO(agentReports);
    }

    private List<String> getAgents() {
        List<String> agents = new ArrayList();

        MongoCursor cursor = mongoTemplate
                .getCollection("Log")
                .distinct("agent", String.class)
                .iterator();

        while (cursor.hasNext()) {
            String agentName = (String) cursor.next();
            agents.add(agentName);
        }

        return agents;
    }

    private List<String> getSourcesForAgent(String agentName) {
        List<Log> sources = logRepository.findByAgentName(agentName);

        List<String> sourcesDistinct = sources.stream()
                .map(log -> log.getSource()).distinct()
                .collect(Collectors.toList());

        return sourcesDistinct;
    }

}
