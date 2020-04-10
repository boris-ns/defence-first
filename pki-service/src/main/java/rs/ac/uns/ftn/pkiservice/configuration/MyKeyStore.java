package rs.ac.uns.ftn.pkiservice.configuration;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;
import rs.ac.uns.ftn.pkiservice.service.CertificateGeneratorService;
import rs.ac.uns.ftn.pkiservice.service.KeyPairGeneratorService;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import static rs.ac.uns.ftn.pkiservice.constants.Constants.*;

@Configuration
public class MyKeyStore {

    @Autowired
    private CertificateGeneratorService certificateGeneratorService;

    @Autowired
    private KeyPairGeneratorService keyPairGeneratorService;

    @Bean(name = "keyStore")
    public KeyStore getKeystore(){
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
            File f = new File(KEYSTORE_FILE_PATH);
            if (f.exists()){
                keyStore.load(new FileInputStream(f), KEYSTORE_PASSWORD);
            }else {
                keyStore.load(null, KEYSTORE_PASSWORD);
                KeyPair kp = keyPairGeneratorService.generateKeyPair();
                X500NameBuilder builder = generateName();

                IssuerData issuerData = certificateGeneratorService.generateIssuerData(kp.getPrivate(), builder.build());
                SubjectData subjectData = certificateGeneratorService.generateSubjectData
                        (kp.getPublic(), builder.build(), Constants.CERT_TYPE.ROOT_CERT);
                subjectData.setSerialNumber(ROOT_ALIAS);
                Certificate certificate = certificateGeneratorService.generateCertificate
                            (subjectData, issuerData,Constants.CERT_TYPE.ROOT_CERT);

                keyStore.setKeyEntry(ROOT_ALIAS, kp.getPrivate(), KEYSTORE_PASSWORD, new Certificate[]{certificate});
                keyStore.store(new FileOutputStream(KEYSTORE_FILE_PATH), KEYSTORE_PASSWORD);
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

    @Bean(name = "archiveKeyStore")
    public KeyStore getArchiveKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
            File f = new File(KEYSTORE_ARCHIVE_FILE_PATH);
            if (f.exists()) {
                keyStore.load(new FileInputStream(f), KEYSTORE_ARCHIVE_PASSWORD);
            } else {
                keyStore.load(null, KEYSTORE_ARCHIVE_PASSWORD);
                keyStore.store(new FileOutputStream(KEYSTORE_ARCHIVE_FILE_PATH), KEYSTORE_PASSWORD);
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
        return builder;
    }

}
