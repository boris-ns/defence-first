package rs.ac.uns.ftn.pkiservice.configuration;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;
import rs.ac.uns.ftn.pkiservice.service.CertificateGeneratorService;
import rs.ac.uns.ftn.pkiservice.service.KeyPairGeneratorService;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class MyKeyStore {

    public static String FILE_PATH = "myKeystore.jks";
    public static char[] PASSWORD = "123456".toCharArray();

    @Autowired
    private CertificateGeneratorService certificateGeneratorService;

    @Autowired
    private KeyPairGeneratorService keyPairGeneratorService;

    @Bean
    public KeyStore getKeystore(){
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
            File f = new File(FILE_PATH);
            if (f.exists()){
                keyStore.load(new FileInputStream(f), PASSWORD);
            }else {
                keyStore.load(null, PASSWORD);
                KeyPair kp = keyPairGeneratorService.generateKeyPair();
                X500NameBuilder builder = generateName();

                IssuerData issuerData = certificateGeneratorService.generateIssuerData(kp.getPrivate(), builder.build());
                SubjectData subjectData = certificateGeneratorService.generateSubjectData
                        (kp.getPublic(), builder.build(), Constants.CERT_TYPE.ROOT_CERT);

                Certificate certificate = certificateGeneratorService.generateCertificate
                            (subjectData, issuerData,Constants.CERT_TYPE.ROOT_CERT);

                keyStore.setKeyEntry("df.pki.root", kp.getPrivate(), PASSWORD, new Certificate[]{certificate});
                keyStore.store(new FileOutputStream(FILE_PATH), PASSWORD);
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
