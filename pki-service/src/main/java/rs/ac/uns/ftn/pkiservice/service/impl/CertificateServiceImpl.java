package rs.ac.uns.ftn.pkiservice.service.impl;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.pkiservice.configuration.MyKeyStore;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;
import rs.ac.uns.ftn.pkiservice.repository.CertificateRepository;
import rs.ac.uns.ftn.pkiservice.repository.KeyStoreRepository;
import rs.ac.uns.ftn.pkiservice.service.CertificateGeneratorService;
import rs.ac.uns.ftn.pkiservice.service.CertificateService;
import rs.ac.uns.ftn.pkiservice.service.KeyPairGeneratorService;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private CertificateGeneratorService certificateGenerator;

    @Autowired
    private KeyPairGeneratorService keyPairGeneratorService;

    @Autowired
    private KeyStoreRepository keyStoreRepository;

    @Override
    public X509Certificate findById(String id) {
//        Certificate certificate = certificateRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Certificate doesn't exist."));

        return null;
    }

    @Override
    public X509Certificate findCertificateByAlias(String alias) throws NoSuchAlgorithmException, CertificateException,
            NoSuchProviderException, KeyStoreException, IOException {
        return (X509Certificate) keyStoreRepository.readCertificate(alias);
    }

    @Override
    public IssuerData findIssuerByAlias(String alias) throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException, UnrecoverableKeyException {
        return  keyStoreRepository.loadIssuer(alias);
    }

    //@TODO: he he
    @Override
    public X509Certificate getCertificateBySerialNumber(BigInteger certificateSerialNumber) {
        return null;
    }

    @Override
    public X509Certificate generateCertificate() throws CertificateException, UnrecoverableKeyException,
            NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, SignatureException,
            InvalidKeyException {

        //admin unosi podatke

        KeyPair keyPairSuject = keyPairGeneratorService.generateKeyPair();
        SubjectData subjectData = generateSubjectData(keyPairSuject);
        //treba da se dobavi na osnovu onoga sto unese admin
        System.out.println(findCertificateByAlias("df.pki.root"));
        System.out.println(findIssuerByAlias("df.pki.root").getX500name());
        IssuerData issuerData = findIssuerByAlias("df.pki.root");

        //Generise se sertifikat za subjekta, potpisan od strane issuer-a
        X509Certificate cert = certificateGenerator.generateCertificate(subjectData, issuerData);

        System.out.println("\n===== Podaci o izdavacu sertifikata =====");
        System.out.println(cert.getIssuerX500Principal().getName());
        System.out.println("\n===== Podaci o vlasniku sertifikata =====");
        System.out.println(cert.getSubjectX500Principal().getName());
        System.out.println("\n===== Sertifikat =====");
        System.out.println("-------------------------------------------------------");
        System.out.println(cert);
        System.out.println("-------------------------------------------------------");

        //Moguce je proveriti da li je digitalan potpis sertifikata ispravan, upotrebom javnog kljuca izdavaoca
        cert.verify(findCertificateByAlias("df.pki.root").getPublicKey());
        System.out.println("\nValidacija uspesna :)");
        // nisam sigurna ciji privatni kljuc treba ovde da idee
        keyStoreRepository.write("novi.alias", keyPairSuject.getPrivate(), MyKeyStore.PASSWORD, cert);
        return cert;
    }


    private IssuerData generateIssuerData(PrivateKey issuerKey) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, "Nikola Luburic");
        builder.addRDN(BCStyle.SURNAME, "Luburic");
        builder.addRDN(BCStyle.GIVENNAME, "Nikola");
        builder.addRDN(BCStyle.O, "UNS-FTN");
        builder.addRDN(BCStyle.OU, "Katedra za informatiku");
        builder.addRDN(BCStyle.C, "RS");
        builder.addRDN(BCStyle.E, "nikola.luburic@uns.ac.rs");
        //UID (USER ID) je ID korisnika
        builder.addRDN(BCStyle.UID, "654321");

        //Kreiraju se podaci za issuer-a, sto u ovom slucaju ukljucuje:
        // - privatni kljuc koji ce se koristiti da potpise sertifikat koji se izdaje
        // - podatke o vlasniku sertifikata koji izdaje nov sertifikat
        return new IssuerData(issuerKey, builder.build());
    }

    private SubjectData generateSubjectData(KeyPair keyPairSubject) {
        try {

            //Datumi od kad do kad vazi sertifikat
            SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = iso8601Formater.parse("2017-12-31");
            Date endDate = iso8601Formater.parse("2022-12-31");

            //Serijski broj sertifikata
            String sn = "1";
            //klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
            X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
            builder.addRDN(BCStyle.CN, "Goran Sladic");
            builder.addRDN(BCStyle.SURNAME, "Sladic");
            builder.addRDN(BCStyle.GIVENNAME, "Goran");
            builder.addRDN(BCStyle.O, "UNS-FTN");
            builder.addRDN(BCStyle.OU, "Katedra za informatiku");
            builder.addRDN(BCStyle.C, "RS");
            builder.addRDN(BCStyle.E, "sladicg@uns.ac.rs");
            //UID (USER ID) je ID korisnika
            builder.addRDN(BCStyle.UID, "123456");

            //Kreiraju se podaci za sertifikat, sto ukljucuje:
            // - javni kljuc koji se vezuje za sertifikat
            // - podatke o vlasniku
            // - serijski broj sertifikata
            // - od kada do kada vazi sertifikat
            return new SubjectData(keyPairSubject.getPublic(), builder.build(), sn, startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
