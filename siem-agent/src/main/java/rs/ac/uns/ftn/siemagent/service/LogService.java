package rs.ac.uns.ftn.siemagent.service;

import rs.ac.uns.ftn.siemagent.model.Log;
import java.util.ArrayList;

public interface LogService {

    void sendLogs(ArrayList<Log> logs);

    void signLogs(ArrayList<Log> logs) throws Exception;
}
