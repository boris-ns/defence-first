package rs.ac.uns.ftn.siemcentar.configuration;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rs.ac.uns.ftn.siemcentar.service.CertificateService;
import rs.ac.uns.ftn.siemcentar.service.KeyPairGeneratorService;

import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.List;

@Configuration
public class KeystoreConfiguration {

    @Value("${keystore.filePath}")
    private String keyStoreFilePath;

    @Value("${trustStore.filePath}")
    private String trustStoreFilePath;

    @Value("${keystore.password}")
    private String keyStorePassword;

//    @Value("${pki.certificate.path}")
//    private String pkiCertFilePath;

    @Value("${root.certificate.path}")
    private String rootCertFilePath;

    @Autowired
    private CertificateService certificateService;

    @Bean(name = "myKeyStore")
    public KeyStore getKeystore(){
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");

            File f = new File(keyStoreFilePath);

            if (f.exists()) {
                keyStore.load(new FileInputStream(f), keyStorePassword.toCharArray());
            } else {
                keyStore.load(null, keyStorePassword.toCharArray());
                keyStore.store(new FileOutputStream(keyStoreFilePath), keyStorePassword.toCharArray());
            }
            return keyStore;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean(name = "trustStore")
    public KeyStore getTruststore(){
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");

            File f = new File(trustStoreFilePath);

            if (f.exists()) {
                keyStore.load(new FileInputStream(f), keyStorePassword.toCharArray());
            } else {
                keyStore.load(null, keyStorePassword.toCharArray());

                CertificateFactory cf = CertificateFactory.getInstance("X.509");

                InputStream certstream2 = certificateService.fullStream(rootCertFilePath);
                Certificate certsRoot =  cf.generateCertificate(certstream2);

                keyStore.setCertificateEntry("root", certsRoot);
                keyStore.store(new FileOutputStream(trustStoreFilePath), keyStorePassword.toCharArray());
            }
            return keyStore;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private X500NameBuilder generateName(){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, "ROOT");
        builder.addRDN(BCStyle.O, "UNS-FTN");
        builder.addRDN(BCStyle.OU, "Katedra za informatiku");
        builder.addRDN(BCStyle.L, "Novi Sad");
        builder.addRDN(BCStyle.C, "RS");
        //UID (USER ID) je ID korisnika
        builder.addRDN(BCStyle.UID, "123456");
        return builder;
    }
}
