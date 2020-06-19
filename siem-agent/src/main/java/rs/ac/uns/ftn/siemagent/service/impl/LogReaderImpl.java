package rs.ac.uns.ftn.siemagent.service.impl;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemagent.model.Log;
import rs.ac.uns.ftn.siemagent.model.LogType;
import rs.ac.uns.ftn.siemagent.service.LogReader;
import rs.ac.uns.ftn.siemagent.service.LogService;
import rs.ac.uns.ftn.siemagent.utils.DateUtil;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

@Service
public class LogReaderImpl implements LogReader {

    @Value("${log.reader.mode}")
    private String logReaderMode;

    @Value("${log.reader.batchInterval}")
    private Integer batchInterval;

    @Value("${log.reader.simulator.path}")
    private String simulatorLogPath;

    @Value("${log.reader.simulator.filter}")
    private String simulatorLogFilter;

    @Value("${agent.name}")
    private String agent;

    @Autowired
    private LogService logService;


    @Override
    public void readLogs() throws IOException, InterruptedException {
        if (logReaderMode.equals("batch")) {
            while (true) {
                this.readLogFromSimulator();
                System.out.println(new Date() + " (Batch interval) Done reading logs");
                Thread.sleep(batchInterval * 60 * 1000);
            }
        } else if (logReaderMode.equals("rt")) {
            while (true) {
                this.readLogFromSimulator();
                System.out.println(new Date() + " (Real time) Done reading logs");
                Thread.sleep(2000);
            }
        }
    }

    private void readLogFromSimulator() throws IOException {
        Date threshold = new Date(System.currentTimeMillis() - batchInterval * 60 * 1000);
        ArrayList<Log> logs = new ArrayList<>();
        ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(simulatorLogPath));

        try {
            String line = reader.readLine();

            while (line != null) {
                Log log = this.parseLogFromSimulator(line);

                if (!log.getDate().after(threshold)) {
                    break;
                }

                if (log.getMessage().matches(simulatorLogFilter)) {
                    System.out.println(line);
                    logs.add(log);
                }

                line = reader.readLine();
            }
        } finally {
            reader.close();
        }

        Collections.reverse(logs);
        logService.sendLogs(logs);
    }

    private Log parseLogFromSimulator(String line) {
        String[] tokens = line.split(" - ");

        Date date = null;
        try {
            date = DateUtil.parse(tokens[0]);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        String source = tokens[1];
        LogType type = LogType.valueOf(tokens[2]);
        String message = tokens[3];

        return new Log(null, date, type, message, source, agent);
    }
}
