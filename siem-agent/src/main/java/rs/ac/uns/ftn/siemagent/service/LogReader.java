package rs.ac.uns.ftn.siemagent.service;

import java.io.IOException;

public interface LogReader {

    void readLogs() throws IOException, InterruptedException;
}
