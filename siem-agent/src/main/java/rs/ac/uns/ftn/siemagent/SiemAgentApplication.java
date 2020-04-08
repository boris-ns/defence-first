package rs.ac.uns.ftn.siemagent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rs.ac.uns.ftn.siemagent.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemagent.service.AuthService;
import rs.ac.uns.ftn.siemagent.service.CertificateService;

import java.io.IOException;

@SpringBootApplication
public class SiemAgentApplication implements CommandLineRunner {

	@Autowired
	private CertificateService certificateService;

	@Autowired
	private AuthService authService;

	public static void main(String[] args) {
		SpringApplication.run(SiemAgentApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException {
		TokenDTO token = authService.login();

		if (token == null) {
			System.out.println("[ERROR] Error while trying to login.");
			return;
		}

		String csr = certificateService.buildCertificateRequest(token);
	}
}
