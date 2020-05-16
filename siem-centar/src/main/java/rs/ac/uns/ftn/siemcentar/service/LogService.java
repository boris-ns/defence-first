package rs.ac.uns.ftn.siemcentar.service;

import rs.ac.uns.ftn.siemcentar.dto.request.LogFilterDTO;
import rs.ac.uns.ftn.siemcentar.model.Log;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public interface LogService {

    void saveLogs(List<Log> logs);

    List<Log> findAll();

    List<Log> searchAndFilter(LogFilterDTO filter);

    SecretKey processPreMasterSecret(X509Certificate clientCertificate, byte[] secretKey) throws Exception;

    byte[] encriptSimetricKeyWithMyKey(SecretKey key) throws Exception;

    byte[] encriptSimetricKey(SecretKey secretKey, PublicKey publicKey) throws Exception;

    byte[] decriptWitmMyPrivate(byte[] data) throws Exception;

    ArrayList<String> convertLogsFromByte(byte[] logs) throws Exception;

}
