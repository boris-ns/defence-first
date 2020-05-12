package rs.ac.uns.ftn.siemagent.service;

import rs.ac.uns.ftn.siemagent.dto.response.TokenDTO;

import javax.crypto.SecretKey;
import javax.net.ssl.HttpsURLConnection;
import java.net.Socket;
import java.security.cert.X509Certificate;

public interface LogService {

    void sendLogs(TokenDTO tokenDTO);

    SecretKey initCommunicationWithSiemCentar(TokenDTO tokenDTO) throws Exception;

    X509Certificate sendSiemCenterHello(TokenDTO tokenDTO) throws Exception;

    byte[] preMasterSecret(X509Certificate certificateCenter, SecretKey secretKey) throws Exception;

    SecretKey sendPreMasterSecret(TokenDTO tokenDTO, byte[] simetricKey) throws Exception;
}
