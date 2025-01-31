package rs.ac.uns.ftn.siemcentar.service;

import rs.ac.uns.ftn.siemcentar.dto.request.LogFilterDTO;
import rs.ac.uns.ftn.siemcentar.model.Log;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public interface LogService {

    void saveLogs(List<Log> logs);

    List<Log> findAll();

    List<Log> searchAndFilter(LogFilterDTO filter);

    void verifyLogsSigns(ArrayList<Log> logs, PublicKey publicKey) throws Exception;


}
