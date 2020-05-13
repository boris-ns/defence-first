package rs.ac.uns.ftn.siemagent.service;

import rs.ac.uns.ftn.siemagent.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemagent.model.Log;

import javax.crypto.SecretKey;
import javax.net.ssl.HttpsURLConnection;
import java.net.Socket;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

public interface LogService {

    void sendLogs(TokenDTO tokenDTO, SecretKey simetricKey, String secureToken, ArrayList<Log> logs);

    Object[] initCommunicationWithSiemCentar(TokenDTO tokenDTO) throws Exception;

    X509Certificate sendSiemCenterHello(TokenDTO tokenDTO) throws Exception;

    byte[] preMasterSecret(X509Certificate certificateCenter, SecretKey secretKey) throws Exception;

    String sendPreMasterSecret(TokenDTO tokenDTO, byte[] simetricKey) throws Exception;
}
