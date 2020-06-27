package rs.ac.uns.ftn.siemagent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rs.ac.uns.ftn.siemagent.repository.Keystore;
import rs.ac.uns.ftn.siemagent.service.CertificateService;
import rs.ac.uns.ftn.siemagent.service.LogReader;
import rs.ac.uns.ftn.siemagent.service.LogService;
import rs.ac.uns.ftn.siemagent.service.OCSPService;

@SpringBootApplication
public class SiemAgentApplication implements CommandLineRunner {

	@Autowired
	private CertificateService certificateService;

	@Autowired
	private OCSPService ocspService;

	@Autowired
	private LogService logService;

	@Autowired
	private Keystore keystore;

	@Autowired
	private LogReader logReader;

	public static void main(String[] args) {
		SpringApplication.run(SiemAgentApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logReader.readLogs();

//		certificateService.createReplaceCertificateRequest();
//		certificateService.createRequestForCertificate();
//		certificateService.installCertificateFromFile(true);


	}


}
