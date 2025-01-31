package rs.ac.uns.ftn.siemagent.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.siemagent.model.Log;
import rs.ac.uns.ftn.siemagent.service.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;


@Service
public class LogServiceImpl implements LogService {

    @Value("${uri.siem.center.send.data}")
    private String httpSiemCentarData;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SignService signService;


    @Override
    public void sendLogs(ArrayList<Log> logs) {
        if (logs.size() < 1){return;}
        try {
            this.signLogs(logs);
            ArrayList<String> stringLogs = convertToStrArray(logs);
            HttpEntity<ArrayList<String>> entityReq = new HttpEntity(stringLogs);
            ResponseEntity<String> responseEntity = null;

            try {
                responseEntity = restTemplate.exchange(httpSiemCentarData, HttpMethod.POST, entityReq, String.class);
            } catch (HttpClientErrorException e) {
                System.out.println("[ERROR] You are not allowed to send Logs");
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void signLogs(ArrayList<Log> logs) throws Exception{
        for(int i=0; i< logs.size(); i++) {
            signService.singLog(logs.get(i));
        }
    }


    private ArrayList<String> convertToStrArray(ArrayList<Log> logs) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ArrayList<String> stringLogs = new ArrayList<>();
        for( Log l : logs) {
            stringLogs.add(ow.writeValueAsString(l));
        }
        return stringLogs;
    }

}
