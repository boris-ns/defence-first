package rs.ac.uns.ftn.siemagent.service;

import java.io.IOException;
import java.util.Date;

public interface LogReader {

    void readLogs() throws Exception;

    Date readLogFromLinux(Date date) throws Exception;

    Date readLogFromKeyCloak(Date threshold, Boolean readLinuxLogs) throws Exception;

    Date readLogFromWindows(Date threshold) throws Exception;
}
