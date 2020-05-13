package rs.ac.uns.ftn.siemcentar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rs.ac.uns.ftn.siemcentar.service.CertificateService;

@SpringBootApplication
public class SiemCentarApplication implements CommandLineRunner {

    @Autowired
    private CertificateService certificateService;

    public static void main(String[] args) {
        SpringApplication.run(SiemCentarApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        certificateService.sendRequestForCertificate(token);
//        certificateService.installCertificateFromFile();
//        X509Certificate certificate = certificateService.findMyCertificate();
//        System.out.println(certificate.getSerialNumber());

    }

}
