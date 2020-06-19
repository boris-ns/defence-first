package rs.ac.uns.ftn.siemcentar.service.impl;

import com.mongodb.client.MongoCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemcentar.dto.request.ReportRequestDTO;
import rs.ac.uns.ftn.siemcentar.dto.response.ReportsDTO;
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

        for (String s : agents) {
            System.out.println(s + " " + logRepository.countLogsByAgentName(s));
            System.out.println("\talarms " + s + " " + alarmRepository.countByAgentName(s));

            List<Log> sources = logRepository.findByAgentName(s);

            List<String> sourcesDistinct = sources.stream()
                    .map(log -> log.getSource()).distinct()
                    .collect(Collectors.toList());
            System.out.println(sourcesDistinct.size());

            for (String source : sourcesDistinct) {
                System.out.println("ZA " + source + " " + logRepository.countLogsBySourceAndAgent(source, s));
                System.out.println("ZA ALARM " + source + " " + alarmRepository.countBySourceAndAgent(source, s));
            }
        }

        return null;
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
        List<String> sources = new ArrayList();

        MongoCursor cursor = mongoTemplate
                .getCollection("Log")

                .distinct("agent", String.class)
                .iterator();

        while (cursor.hasNext()) {
            String sourceName = (String) cursor.next();
            sources.add(sourceName);
        }

        return sources;
    }

    private int getNumOfLogsForAgent(String agentName) {

        return 0;
    }

    private int getNumOfAlarmsForAgent(String agentName) {

        return 0;
    }
}
