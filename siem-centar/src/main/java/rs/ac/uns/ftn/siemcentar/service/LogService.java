package rs.ac.uns.ftn.siemcentar.service;

import rs.ac.uns.ftn.siemcentar.model.Log;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public interface LogService {

    void saveLogs(List<Log> logs);

    List<Log> findAll();

}
