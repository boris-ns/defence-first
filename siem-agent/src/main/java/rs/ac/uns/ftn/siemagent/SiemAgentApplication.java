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


		System.out.println("mogu da se pokrenem lepo");

		// simulacija slanja logova
//		simulation();

//		certificateService.sendRequestForCertificate();
//		certificateService.installCertificateFromFile();


////		//@TODO moguce da spojimo u jednu metodu al ovakav proces treba da bude...
//		X509Certificate certificate = certificateService.getCertificateBySerialNumber("1586552702410", token);
//		OCSPReq request = ocspService.generateOCSPRequest(certificate, token);
//		OCSPResp response = ocspService.sendOCSPRequest(request, token);
//		boolean val = ocspService.processOCSPResponse(request,response, token);
//		System.out.println(val);


//		X509Certificate certificate = certificateService.findMyCertificate();
//		certificateService.sendRequestForCertificate(token);
//		certificateService.sendReplaceCertificateRequest(token);
	}


}
