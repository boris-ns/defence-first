package rs.ac.uns.ftn.pkiservice;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rs.ac.uns.ftn.pkiservice.repository.KeyStoreRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.StringWriter;
import java.security.PrivateKey;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;

import static rs.ac.uns.ftn.pkiservice.constants.Constants.ANGULAR_ALIAS;
import static rs.ac.uns.ftn.pkiservice.constants.Constants.ROOT_ALIAS;

@SpringBootApplication
public class PkiServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PkiServiceApplication.class, args);
	}

	@Autowired
	private KeyStoreRepository keystore;

	@Value("${generated.certifacates.directory}")
	private String certDirectory;

	@Override
	public void run(String... args) throws Exception {

		PrivateKey privateKey = keystore.readPrivateKey(ANGULAR_ALIAS);

		StringWriter sw = new StringWriter();
		JcaPEMWriter pm = new JcaPEMWriter(sw);
		pm.writeObject(privateKey);
		pm.close();

		String fileName = "cert_angular.key";
		String path = certDirectory + "/" + fileName;


		try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
			writer.write(sw.toString());
		}


	}
}