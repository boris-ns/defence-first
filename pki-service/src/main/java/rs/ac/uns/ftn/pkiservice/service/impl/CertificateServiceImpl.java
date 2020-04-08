package rs.ac.uns.ftn.pkiservice.service.impl;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.thymeleaf.util.ArrayUtils;
import rs.ac.uns.ftn.pkiservice.configuration.MyKeyStore;
import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;
import rs.ac.uns.ftn.pkiservice.repository.KeyStoreRepository;
import rs.ac.uns.ftn.pkiservice.service.CertificateGeneratorService;
import rs.ac.uns.ftn.pkiservice.service.CertificateService;
import rs.ac.uns.ftn.pkiservice.service.KeyPairGeneratorService;

import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.stream.Collectors;

import static rs.ac.uns.ftn.pkiservice.constants.Constants.KEYSTORE_PASSWORD;
import static rs.ac.uns.ftn.pkiservice.constants.Constants.ROOT_ALIAS;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateGeneratorService certificateGenerator;

    @Autowired
    private KeyPairGeneratorService keyPairGeneratorService;

    @Autowired
    private KeyStoreRepository keyStoreRepository;

    @Override
    public List<X509Certificate> findAll() {
        List<Certificate> certificateList = keyStoreRepository.readAll();
        return certificateList.stream().map(X509Certificate.class::cast).collect(Collectors.toList());
    }

    @Override
    public X509Certificate findCertificateByAlias(String alias) {
        return (X509Certificate) keyStoreRepository.readCertificate(alias);
    }

    @Override
    public IssuerData findIssuerByAlias(String alias) throws NoSuchAlgorithmException, CertificateException,
            KeyStoreException, IOException, UnrecoverableKeyException {
        return  keyStoreRepository.loadIssuer(alias);
    }

    @Override
    public Certificate getCertificateByAlias(String alias) {
        return keyStoreRepository.readCertificate(alias);
    }

    @Override
    public Certificate[] getCertificateChainByAlias(String alias) {
        return keyStoreRepository.readCertificateChain(alias);
    }

    /*
    * tip sertifikata koji pravimo kako bi se dodale odgovarajuce ekstenzije
    * dodati ostale parametre
    * */

    @Override
    public X509Certificate generateCertificateIntermediate(String subjectName, String issuerAlias)
            throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException,
            IOException {
        KeyPair keyPairSuject = keyPairGeneratorService.generateKeyPair();
        //promeniti kreiranje X500Name na osnovu subject name
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
        SubjectData subjectData = certificateGenerator.generateSubjectData(keyPairSuject.getPublic(), builder.build(),
                Constants.CERT_TYPE.INTERMEDIATE_CERT);
        IssuerData issuerData = findIssuerByAlias(issuerAlias);




        // @TODO: da ga doda u lanac, DA IMAMO METODU ZA TO da PRONADJE LANAC I DA GA SACUVA

        Certificate[] certificatesChainIssuer = getCertificateChainByAlias(issuerAlias);

        //Generise se sertifikat za subjekta, potpisan od strane issuer-a
        X509Certificate cert = certificateGenerator.generateCertificate(subjectData, issuerData,
                Constants.CERT_TYPE.INTERMEDIATE_CERT);

        int size = certificatesChainIssuer.length;
        Certificate[] certificatesChain = new Certificate[size + 1];
        certificatesChain[0] = cert;
        for (int i = 0; i < size; i++) {
            certificatesChain[i + 1] = certificatesChainIssuer[i];
        }


        keyStoreRepository.writeKeyEntry(cert.getSerialNumber().toString(), keyPairSuject.getPrivate(), certificatesChain);
        return cert;
    }

    //@TODO: i DA CUVA LANAC
    //@TODO: resiti OVDE KAD NIJE LEAF sert za privatni kljuc da se ne prosledjuje Null
    @Override
    public void writeCertificateToKeyStore(X509Certificate cert, Constants.CERT_TYPE certType, PrivateKey pk)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        if (!certType.equals(Constants.CERT_TYPE.LEAF_CERT)) {
            keyStoreRepository.writeKeyEntry(cert.getSerialNumber().toString(), pk, new Certificate[]{cert});
        }
        else {
            keyStoreRepository.writeCertificateEntry(cert.getSerialNumber().toString(), cert);
        }
    }

    /*TODO: impelmentirati da se svi zahtevi za sertifikate prikazu*/
    @Override
    public List<X509Certificate> findAllRequests() {
        return null;
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

        return certificateGenerator.generateIssuerData(issuerKey, builder.build());
        //Kreiraju se podaci za issuer-a, sto u ovom slucaju ukljucuje:
        // - privatni kljuc koji ce se koristiti da potpise sertifikat koji se izdaje
        // - podatke o vlasniku sertifikata koji izdaje nov sertifikat
    }

    private SubjectData generateSubjectData(KeyPair keyPairSubject) {
        //klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, "Goran Sladic");
        builder.addRDN(BCStyle.SURNAME, "Sladic");
        builder.addRDN(BCStyle.GIVENNAME, "Goran");
        builder.addRDN(BCStyle.O, "UNS-FTN");
        builder.addRDN(BCStyle.OU, "Katedra za informatiku");
        builder.addRDN(BCStyle.C, "RS");
        builder.addRDN(BCStyle.E, "sladicg@uns.ac.rs");

        // @TODO: UID (USER ID) je ID korisnika. Kog korisnika ?
        builder.addRDN(BCStyle.UID, "123456");
        X500Name name = builder.build();

        return  certificateGenerator.generateSubjectData
                (keyPairSubject.getPublic(),name,Constants.CERT_TYPE.LEAF_CERT);
    }
}
