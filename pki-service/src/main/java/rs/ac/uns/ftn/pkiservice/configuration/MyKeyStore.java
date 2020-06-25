package rs.ac.uns.ftn.pkiservice.configuration;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;
import rs.ac.uns.ftn.pkiservice.service.CertificateGeneratorService;
import rs.ac.uns.ftn.pkiservice.service.CertificateService;
import rs.ac.uns.ftn.pkiservice.service.KeyPairGeneratorService;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static rs.ac.uns.ftn.pkiservice.constants.Constants.*;

@Configuration
public class MyKeyStore {

    @Autowired
    private CertificateGeneratorService certificateGeneratorService;

    @Autowired
    private KeyPairGeneratorService keyPairGeneratorService;

    @Value("${keystore.filepath}")
    private String KEYSTORE_FILE_PATH;

    @Value("${keystore.password}")
    private String KEYSTORE_PASSWORD;

    @Value("${keystore.archive.filepath}")
    private String KEYSTORE_ARCHIVE_FILE_PATH;

    @Value("${keystore.archive.password}")
    private String KEYSTORE_ARCHIVE_PASSWORD;

    @Value("${truststore.path}")
    private String TRUSTSTORE_FILE_PATH;

    @Value("${truststore.password}")
    private String TRUSTSTORE_PASSWORD;


    @Value("${generated.certifacates.directory}")
    private String certDirectory;

    @Bean(name = "keyStore")
    public KeyStore getKeystore(){
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
            KeyStore trustStore = KeyStore.getInstance("JKS", "SUN");
            File f = new File(KEYSTORE_FILE_PATH);
            if (f.exists()){
                keyStore.load(new FileInputStream(f), KEYSTORE_PASSWORD.toCharArray());
            }else {
                keyStore.load(null, KEYSTORE_PASSWORD.toCharArray());
                trustStore.load(null, TRUSTSTORE_PASSWORD.toCharArray());

                X509Certificate root_cert = generateRoot(keyStore);
                this.writeCertToFile(keyStore, ROOT_ALIAS);
                trustStore.setCertificateEntry(ROOT_ALIAS, root_cert);

                X509Certificate pki_cert = generatePKICert(keyStore, root_cert);
                this.writeCertToFile(keyStore,PKI_ALIAS);
//                trustStore.setCertificateEntry(PKI_ALIAS, pki_cert);

                X509Certificate pki_communication_cert = generatePKICommunicationCert(keyStore, pki_cert, root_cert);
                this.writeCertToFile(keyStore,PKI_COMMUNICATION_ALIAS);

                X509Certificate angularCert = generateAngularCertificate(keyStore, pki_cert, root_cert);
                this.writeCertToFile(keyStore, ANGULAR_ALIAS);

                X509Certificate keyCloakCert = generateKeyCloakCertificate(keyStore, root_cert);
                this.writeCertToFile(keyStore, KEY_CLOAK_ALIAS);

                trustStore.store(new FileOutputStream(TRUSTSTORE_FILE_PATH), TRUSTSTORE_PASSWORD.toCharArray());
                keyStore.store(new FileOutputStream(KEYSTORE_FILE_PATH), KEYSTORE_PASSWORD.toCharArray());
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
        } catch (Exception e) {
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
                keyStore.load(new FileInputStream(f), KEYSTORE_ARCHIVE_PASSWORD.toCharArray());
            } else {
                keyStore.load(null, KEYSTORE_ARCHIVE_PASSWORD.toCharArray());
                keyStore.store(new FileOutputStream(KEYSTORE_ARCHIVE_FILE_PATH), KEYSTORE_PASSWORD.toCharArray());
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

    private X500NameBuilder generateName(String CN){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, CN);
        builder.addRDN(BCStyle.O, "UNS-FTN");
        builder.addRDN(BCStyle.OU, "Katedra za informatiku");
        builder.addRDN(BCStyle.L, "Novi Sad");
        builder.addRDN(BCStyle.C, "RS");
        return builder;
    }


    private X509Certificate generateRoot(KeyStore keyStore) throws KeyStoreException {
        KeyPair kp = keyPairGeneratorService.generateKeyPair();

        X500NameBuilder builder = generateName("ROOT");

        IssuerData issuerData =
                certificateGeneratorService.generateIssuerData(kp.getPrivate(), builder.build(), kp.getPublic());
        SubjectData subjectData = certificateGeneratorService.generateSubjectData
                (kp.getPublic(), builder.build(), Constants.CERT_TYPE.ROOT_CERT);

        subjectData.setSerialNumber(ROOT_ALIAS);
        Certificate certificate = certificateGeneratorService.generateCertificate
                (subjectData, issuerData,Constants.CERT_TYPE.ROOT_CERT);

        keyStore.setKeyEntry(ROOT_ALIAS, kp.getPrivate(),
                KEYSTORE_PASSWORD.toCharArray(), new Certificate[]{certificate});

        return (X509Certificate) certificate;
    }

    private X509Certificate generatePKICert(KeyStore keyStore, X509Certificate root) throws Exception{
        KeyPair kp = keyPairGeneratorService.generateKeyPair();

        X500NameBuilder builder = generateName("PKI");
        X500NameBuilder issuerBuilder = generateName("ROOT");

        PrivateKey issuerPrivateKey = (PrivateKey) keyStore.getKey(ROOT_ALIAS, KEYSTORE_PASSWORD.toCharArray());

        IssuerData issuerData =
                certificateGeneratorService.generateIssuerData(
                        issuerPrivateKey, issuerBuilder.build(), root.getPublicKey());

        SubjectData subjectData = certificateGeneratorService.generateSubjectData
                (kp.getPublic(), builder.build(), CERT_TYPE.INTERMEDIATE_CERT);

        subjectData.setSerialNumber(PKI_ALIAS);
        Certificate certificate = certificateGeneratorService.generateCertificate
                (subjectData, issuerData, CERT_TYPE.INTERMEDIATE_CERT);

        keyStore.setKeyEntry(PKI_ALIAS, kp.getPrivate(),
                KEYSTORE_PASSWORD.toCharArray(), new Certificate[]{certificate, root});

        return (X509Certificate) certificate;
    }

    private X509Certificate generatePKICommunicationCert(KeyStore keyStore, X509Certificate pki_cert, X509Certificate root) throws Exception {
        KeyPair kp = keyPairGeneratorService.generateKeyPair();

        X500NameBuilder builder = generateName("pki_communication");
        X500NameBuilder issuerBuilder = generateName("PKI");

        PrivateKey issuerPrivateKey = (PrivateKey) keyStore.getKey(PKI_ALIAS, KEYSTORE_PASSWORD.toCharArray());

        IssuerData issuerData =
                certificateGeneratorService.generateIssuerData(
                        issuerPrivateKey, issuerBuilder.build(), pki_cert.getPublicKey());

        SubjectData subjectData = certificateGeneratorService.generateSubjectData
                (kp.getPublic(), builder.build(), CERT_TYPE.SERVER_CERT);

        subjectData.setSerialNumber(PKI_COMMUNICATION_ALIAS);
        Certificate certificate = certificateGeneratorService.generateCertificate
                (subjectData, issuerData, CERT_TYPE.SERVER_CERT);

        keyStore.setKeyEntry(PKI_COMMUNICATION_ALIAS, kp.getPrivate(),
                KEYSTORE_PASSWORD.toCharArray(), new Certificate[]{certificate, pki_cert, root});

        return (X509Certificate) certificate;
    }


    private X509Certificate generateAngularCertificate(KeyStore keyStore, X509Certificate pki_cert, X509Certificate root) throws Exception {
        KeyPair kp = keyPairGeneratorService.generateKeyPair();

        X500NameBuilder builder = generateName("angular");
        X500NameBuilder issuerBuilder = generateName("PKI");

        PrivateKey issuerPrivateKey = (PrivateKey) keyStore.getKey(PKI_ALIAS, KEYSTORE_PASSWORD.toCharArray());

        IssuerData issuerData =
                certificateGeneratorService.generateIssuerData(
                        issuerPrivateKey, issuerBuilder.build(), pki_cert.getPublicKey());

        SubjectData subjectData = certificateGeneratorService.generateSubjectData
                (kp.getPublic(), builder.build(), CERT_TYPE.LEAF_CERT);

        subjectData.setSerialNumber(ANGULAR_ALIAS);
        Certificate certificate = certificateGeneratorService.generateCertificate
                (subjectData, issuerData, CERT_TYPE.LEAF_CERT);

        keyStore.setKeyEntry(ANGULAR_ALIAS, kp.getPrivate(),
                KEYSTORE_PASSWORD.toCharArray(), new Certificate[]{certificate, pki_cert, root});

        return (X509Certificate) certificate;
    }

    private X509Certificate generateKeyCloakCertificate(KeyStore keyStore, X509Certificate root_cert) throws Exception{
        KeyPair kp = keyPairGeneratorService.generateKeyPair();

        X500NameBuilder builder = generateName("KEY_CLOAK");
        X500NameBuilder issuerBuilder = generateName("ROOT");

        PrivateKey issuerPrivateKey = (PrivateKey) keyStore.getKey(ROOT_ALIAS, KEYSTORE_PASSWORD.toCharArray());

        IssuerData issuerData =
                certificateGeneratorService.generateIssuerData(
                        issuerPrivateKey, issuerBuilder.build(), root_cert.getPublicKey());

        SubjectData subjectData = certificateGeneratorService.generateSubjectData
                (kp.getPublic(), builder.build(), CERT_TYPE.LEAF_CERT);

        subjectData.setSerialNumber(KEY_CLOAK_ALIAS);
        Certificate certificate = certificateGeneratorService.generateCertificate
                (subjectData, issuerData, CERT_TYPE.LEAF_CERT);

        keyStore.setKeyEntry(KEY_CLOAK_ALIAS, kp.getPrivate(),
                KEYSTORE_PASSWORD.toCharArray(), new Certificate[]{certificate, root_cert});

        return (X509Certificate) certificate;
    }




    private void writeCertToFile(KeyStore keyStore, String alias) throws Exception {

        Certificate[] chain = keyStore.getCertificateChain(alias);

        StringWriter sw = new StringWriter();
        JcaPEMWriter pm = new JcaPEMWriter(sw);
        for(Certificate certificate : chain) {
            X509Certificate a = (X509Certificate)certificate;
            pm.writeObject(a);
        }
        pm.close();


        String fileName = "cert_" + alias + ".crt";
        String path = certDirectory + "/" + fileName;


        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(sw.toString());
        }
    }

    public String getKEYSTORE_FILE_PATH() {
        return KEYSTORE_FILE_PATH;
    }

    public char[] getKEYSTORE_PASSWORD() {
        return KEYSTORE_PASSWORD.toCharArray();
    }

    public String getKEYSTORE_ARCHIVE_FILE_PATH() {
        return KEYSTORE_ARCHIVE_FILE_PATH;
    }

    public char[] getKEYSTORE_ARCHIVE_PASSWORD() {
        return KEYSTORE_ARCHIVE_PASSWORD.toCharArray();
    }
}
