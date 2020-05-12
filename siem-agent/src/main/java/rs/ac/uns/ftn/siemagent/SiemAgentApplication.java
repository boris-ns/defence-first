package rs.ac.uns.ftn.siemagent;

import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rs.ac.uns.ftn.siemagent.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemagent.service.AuthService;
import rs.ac.uns.ftn.siemagent.service.CertificateService;
import rs.ac.uns.ftn.siemagent.service.LogService;
import rs.ac.uns.ftn.siemagent.service.OCSPService;

import java.security.cert.X509Certificate;

@SpringBootApplication
public class SiemAgentApplication implements CommandLineRunner {

	@Autowired
	private CertificateService certificateService;

	@Autowired
	private AuthService authService;

	@Autowired
	private OCSPService ocspService;

	@Autowired
	private LogService logService;

	public static void main(String[] args) {
		SpringApplication.run(SiemAgentApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		TokenDTO token = authService.login();

		if (token == null) {
			System.out.println("[ERROR] Error while trying to login.");
			return;
		}

		logService.sendLogs(token);

////		//@TODO moguce da spojimo u jednu metodu al ovakav proces treba da bude...
//		X509Certificate certificate = certificateService.getCertificateBySerialNumber("1586552702410", token);
//		OCSPReq request = ocspService.generateOCSPRequest(certificate, token);
//		OCSPResp response = ocspService.sendOCSPRequest(request, token);
//		boolean val = ocspService.processOCSPResponse(request,response, token);
//		System.out.println(val);




//		certificateService.installCertificateFromFile("noviSertifikat");
//		X509Certificate certificate = certificateService.findMyCertificate();
//		certificateService.sendRequestForCertificate(token);
//		certificateService.sendReplaceCertificateRequest(token);





	}
}
