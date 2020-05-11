package rs.ac.uns.ftn.siemcentar.service;

import rs.ac.uns.ftn.siemcentar.model.Log;

import java.util.List;

public interface LogService {

    void saveLogs(List<Log> logs);

    List<Log> findAll();
}
