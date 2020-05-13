package rs.ac.uns.ftn.siemagent.service;

import rs.ac.uns.ftn.siemagent.model.Log;

import javax.crypto.SecretKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

public interface LogService {

    void sendLogs(SecretKey simetricKey, String secureToken, ArrayList<Log> logs);

    Object[] initCommunicationWithSiemCentar() throws Exception;

    X509Certificate sendSiemCenterHello() throws Exception;

    byte[] preMasterSecret(X509Certificate certificateCenter, SecretKey secretKey) throws Exception;

    String sendPreMasterSecret(byte[] simetricKey) throws Exception;
}
