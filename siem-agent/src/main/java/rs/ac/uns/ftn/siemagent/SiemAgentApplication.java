package rs.ac.uns.ftn.siemagent;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rs.ac.uns.ftn.siemagent.Constants.Constants;
import rs.ac.uns.ftn.siemagent.model.Log;
import rs.ac.uns.ftn.siemagent.model.LogType;
import rs.ac.uns.ftn.siemagent.repository.Keystore;
import rs.ac.uns.ftn.siemagent.service.CertificateService;
import rs.ac.uns.ftn.siemagent.service.LogReader;
import rs.ac.uns.ftn.siemagent.service.LogService;
import rs.ac.uns.ftn.siemagent.service.OCSPService;

import javax.crypto.SecretKey;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Date;

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

	// TODO: obrisati
	private void simulation() throws Exception{

//		ArrayList<Log> logs = new ArrayList<Log>();
//		logs.add(new Log(1l, new Date(), LogType.SUCCESS, "prviLog", "ja"));
//		logs.add(new Log(2l, new Date(), LogType.SUCCESS, "drugiLog", "ja"));
//		logService.sendLogs(logs);
//
//		logs.clear();
//		logs.add(new Log(3l, new Date(), LogType.ERROR, "treciLog", "ja"));
//		logs.add(new Log(4l, new Date(), LogType.WARN, "certvrtiLog", "ja"));

//		logService.sendLogs(logs);
	}

}
