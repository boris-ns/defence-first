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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
        List<String> agents = this.getAgents();
        List<AgentReportDTO> agentReports = new ArrayList<>();

        for (String agent : agents) {
            Long agentNumOfLogs = logRepository.countLogsByAgentName(agent);
            Long agentNumOfAlarms = alarmRepository.countByAgentName(agent);

            List<SourceReportDTO> sourcesReports = new ArrayList<>();

//            System.out.println(agent + " " + logRepository.countLogsByAgentName(agent));
//            System.out.println("\talarms " + agent + " " + alarmRepository.countByAgentName(agent));

            List<String> sources = this.getSourcesForAgent(agent);

            for (String source : sources) {
                Long sourceNumOfLogs = logRepository.countLogsBySourceAndAgent(source, agent);
                Long sourceNumOfAlarms = alarmRepository.countBySourceAndAgent(source, agent);
                SourceReportDTO sourceReportDTO = new SourceReportDTO(source, sourceNumOfLogs, sourceNumOfAlarms);
                sourcesReports.add(sourceReportDTO);
//                System.out.println("ZA " + source + " " + logRepository.countLogsBySourceAndAgent(source, agent));
//                System.out.println("ZA ALARM " + source + " " + alarmRepository.countBySourceAndAgent(source, agent));
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
