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
//        certificateService.installCertificateFromFile();
//        X509Certificate certificate = certificateService.findMyCertificate();
//        System.out.println(certificate.getSerialNumber());

    }


}
