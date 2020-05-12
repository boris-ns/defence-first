package rs.ac.uns.ftn.siemcentar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rs.ac.uns.ftn.siemcentar.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.service.AuthService;
import rs.ac.uns.ftn.siemcentar.service.CertificateService;
import rs.ac.uns.ftn.siemcentar.service.LogService;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@SpringBootApplication
public class SiemCentarApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SiemCentarApplication.class, args);
    }

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private AuthService authService;

    @Override
    public void run(String... args) throws Exception {
        TokenDTO token = authService.login();

        if (token == null) {
            System.out.println("[ERROR] Error while trying to login.");
            return;
        }

//        certificateService.sendRequestForCertificate(token);
//        certificateService.installCertificateFromFile("noviSertifikat");
//        X509Certificate certificate = certificateService.findMyCertificate();
//        System.out.println(certificate.getSerialNumber());

    }

    //TODO: skontati da kako u ove metode ubaciti api poziv ka PKI-u i onda proveriti lanac koji ti posalje SiemCentar
    private static SSLContext createSSLContext() throws Exception {
        TrustManager[] byPassTrustManagers = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
                System.out.println("checkClientTrusted");
            }
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                System.out.println("checkServerTrusted");
            }
        } };
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, byPassTrustManagers, new SecureRandom());
        return sslContext;
    }

//    ServerSocketFactory factory = createSSLContext().getServerSocketFactory();
//
//        try (ServerSocket listener = factory.createServerSocket(8448)) {
//        ((SSLServerSocket) listener).setNeedClientAuth(true);
//        ((SSLServerSocket) listener).setEnabledCipherSuites(
//                new String[] { "TLS_DHE_DSS_WITH_AES_256_CBC_SHA256"});
//        ((SSLServerSocket) listener).setEnabledProtocols(
//                new String[] { "TLSv1.2"});
//        while (true) {
//            try (Socket socket = listener.accept()) {
//                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//                out.println("Hello World!");
//            }
//        }
//    }










}
