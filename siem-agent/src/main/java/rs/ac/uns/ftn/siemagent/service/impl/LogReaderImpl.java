package rs.ac.uns.ftn.siemagent.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.siemagent.model.Log;
import rs.ac.uns.ftn.siemagent.model.LogType;
import rs.ac.uns.ftn.siemagent.service.LogReader;
import rs.ac.uns.ftn.siemagent.service.LogService;
import rs.ac.uns.ftn.siemagent.utils.DateUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Value("${read.keycloak.logs}")
    private Boolean readKeyCloakLogs;

    @Value("${read.simulator.logs}")
    private Boolean readSimulator;

    @Value("${keycloak.base.path}")
    private String keyCloakBasePath;


    @Autowired
    private LogService logService;


    @Override
    public void readLogs() throws Exception {

        String sysName = System.getProperty("os.name");
        Boolean readLinuxLogs = sysName.equalsIgnoreCase("Linux");
        Boolean readWindowsLogs = sysName.contains("Windows");


        // Date threshold = new GregorianCalendar(2020, Calendar.JUNE, 22).getTime();
        Date threshold = new Date();
        if (!logReaderMode.equals("batch") && !logReaderMode.equals("rt")) {
            return;
        }
        while (true) {
            if(readLinuxLogs) {
                threshold = this.readLogFromLinux(threshold);
            }
            if (readWindowsLogs) {
                threshold = this.readLogFromWindows(threshold);
            }
            if(readSimulator) {
                threshold = this.readLogFromSimulator(threshold);
            }
            if(readKeyCloakLogs) {
                threshold = this.readLogFromKeyCloak(threshold, readLinuxLogs);
            }
            if (logReaderMode.equals("batch")) {
                System.out.println(new Date() + " (Batch interval) Done reading logs");
                Thread.sleep(batchInterval * 60 * 1000);
            } else if (logReaderMode.equals("rt")) {
                System.out.println(new Date() + " (Real time) Done reading logs");
                Thread.sleep(2000);
            }
        }
    }

    private Date readLogFromSimulator(Date threshold) throws IOException {
        System.out.println(threshold);
        ArrayList<Log> logs = new ArrayList<>();
        ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(simulatorLogPath));
        Date newThreshold = new Date();
        try {
            String line = reader.readLine();
            boolean setNewThreshold = false;
            while (line != null) {
                Log log = this.parseLogFromSimulator(line);

                if (!setNewThreshold) {
                    newThreshold = log.getDate();
                    setNewThreshold = true;
                }

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
        System.out.println(logs.size());
        logService.sendLogs(logs);
        return newThreshold;
    }


    @Override
    public Date readLogFromLinux(Date threshold) throws Exception {
        ArrayList<Log> logs = new ArrayList<>();
        ArrayList<String> commands = new ArrayList<>();
        commands.add("tac");
        commands.add("../../var/log/auth.log");
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(new File(System.getProperty("user.home")));
        Process process = processBuilder.start();
        Date newThreshold;
        // for reading the ouput from stream
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String s = null;
        boolean setNewThreshold = false;
        while ((s = stdInput.readLine()) != null)
        {
            Log log = this.parseLogFromLinux(s);
            if (log.getDate().before(threshold)) {
                break;
            }
            if (log.getMessage().matches(simulatorLogFilter)) {
                System.out.println(s);
                logs.add(log);
            }
        }

        if(logs.size() > 0) {
            Calendar c = Calendar.getInstance();
            c.setTime(logs.get(0).getDate());
            c.add(Calendar.SECOND, 1);
            newThreshold = c.getTime();
            Collections.reverse(logs);
            logService.sendLogs(logs);
        }
        else{
            newThreshold = threshold;
        }
        return newThreshold;
    }

    @Override
    public Date readLogFromKeyCloak(Date threshold, Boolean readLinuxLogs) throws Exception {
        ArrayList<Log> logs = new ArrayList<>();
        String baseDir = System.getProperty("user.home");
        baseDir += keyCloakBasePath ;
        baseDir += "server.log";
        ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(baseDir));
        Date newThreshold;

        boolean setNewThreshold = false;
        String s = reader.readLine();
        while (s != null) {
            Log log = this.parseLogFromKeyCloak(s);
            if (log.getDate().before(threshold)) {
                break;
            }
            if (log.getMessage().matches(simulatorLogFilter)) {
                System.out.println(s);
                logs.add(log);
            }
            s = reader.readLine();
        }

        if(logs.size() > 0) {
            Calendar c = Calendar.getInstance();
            c.setTime(logs.get(0).getDate());
            c.add(Calendar.SECOND, 1);
            newThreshold = c.getTime();
            Collections.reverse(logs);
            logService.sendLogs(logs);
        }
        else{
            newThreshold = threshold;
        }
        return newThreshold;
    }

    @Override
    public Date readLogFromWindows(Date threshold) throws Exception {
        //String command = "powershell.exe  your command";
        //Getting the version
        SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy HH:mm:ss");

        System.out.println(format.format(threshold));
        String command = "powershell.exe  Get-EventLog -LogName Application -After '" + format.format(threshold) +
                "' | ConvertTo-Json";
        // Executing the command
        Process powerShellProcess = Runtime.getRuntime().exec(command);
        // Getting the results
        powerShellProcess.getOutputStream().close();
        String line;
        BufferedReader stdout = new BufferedReader(new InputStreamReader(
                powerShellProcess.getInputStream()));
        String json = "";
        while ((line = stdout.readLine()) != null) {
            json += line;
        }
        stdout.close();

        if (json == "") {
            return threshold;
        }

        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap[] data = mapper.readValue(json, LinkedHashMap[].class);

        ArrayList<Log> logs = new ArrayList<>();
        boolean setThreshold = false;
        for (LinkedHashMap lhm: data) {
            Log log = parseLogFromWindows(lhm);
            if (!setThreshold){
                threshold = log.getDate();
                setThreshold = true;
            }
            logs.add(log);
        }

        Collections.reverse(logs);
        logService.sendLogs(logs);
        return threshold;
    }

    private Log parseLogFromKeyCloak(String s) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] allData = s.split(",");
        String date  = allData[0];

        int startIndxSource = allData[1].indexOf("[");
        int endIndxSource = allData[1].indexOf(")");
        String source = allData[1].substring(startIndxSource, endIndxSource+1);

        String logType = allData[1].split(" ")[1];

        LogType type;
        switch (logType) {
            case "WARN":
                type = LogType.WARN;
                break;
            case "ERROR":
                type = LogType.ERROR;
                break;
            default:
                type = LogType.INFO;
                break;
        }

        String message = String.join(" ", allData);
        if(allData[1].contains("type=LOGIN_ERROR")) {
                int usernameIndxStart = message.indexOf("username=");
                int usernameIndxEnd = message.indexOf(" ", usernameIndxStart);
                String username = message.substring(usernameIndxStart, usernameIndxEnd+1);
                message = "failed login," + username;
        }

        Log l = new Log(null, simpleDateFormat.parse(date), type, message, source, agent);
        return l;

    }

    private Log parseLogFromLinux(String s) throws Exception{
        String date = s.substring(0, 15);
        String data[] = s.substring(16).split(": ");
        String source = data[0];
        String message = data[data.length-1];
        LogType type = LogType.INFO;

        //@TODO: hocete WARN ili ERROR?
        if( message.contains("authentication failure") && !data[1].contains("message repeated")) {
            type = LogType.WARN;
            message = "AUTHENTICATION FAILURE ON MACHINE";
        }

        if( message.contains("authentication failure") && data[1].contains("message repeated")) {
            type = LogType.WARN;
            message = "MULTIPLE SUCCESSIVE AUTHENTICATION FAILURE ON MACHINE in ONE SECOND";
        }


        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd HH:mm:ss");
        c.setTime(simpleDateFormat.parse(date));
        c.set(Calendar.YEAR, year);

        Log l = new Log(null, c.getTime(), type, message, source, agent);
        return l;
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

    private Log parseLogFromWindows(LinkedHashMap<String, Object> data) {
        String d = (String) data.get("TimeGenerated");
        int t = (int) data.get("EntryType");
        String message = (String) data.get("Message");
        String source = (String) data.get("Source");
        LogType type;
// ovima kojima je dodeljena vrednost je tako za success nisam sigurna ima jos jedno polje isto ne znam koja je vrednost
        switch (t) {
            case 1:
                type = LogType.ERROR;
                break;
            case 2:
                type = LogType.WARN;
                break;
            case 4:
                type = LogType.INFO;
                break;
            default:
                type = LogType.SUCCESS;
                break;
        }

        d = d.substring(d.indexOf("(") + 1);
        d = d.substring(0, d.indexOf(")"));
        Date date = new Date(Long.parseLong(d));

        return new Log(null, date, type, message, source, agent);
    }
}
