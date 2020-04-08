package rs.ac.uns.ftn.pkiservice.service.impl;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;
import rs.ac.uns.ftn.pkiservice.repository.KeyStoreRepository;
import rs.ac.uns.ftn.pkiservice.service.CertificateGeneratorService;
import rs.ac.uns.ftn.pkiservice.service.CertificateService;
import rs.ac.uns.ftn.pkiservice.service.KeyPairGeneratorService;

import javax.security.auth.x500.X500PrivateCredential;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<X500PrivateCredential> findAllRootAndIntermediate() {
        return keyStoreRepository.readCertificateAndAliasForRootAndIntermediate();
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
    public X509Certificate generateCertificateIntermediate(HashMap<String, String> subjectName, String issuerAlias)
            throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException,
            IOException {
        KeyPair keyPairSuject = keyPairGeneratorService.generateKeyPair();
        X500Name name = certificateGenerator.generateName(subjectName);
        SubjectData subjectData = certificateGenerator.generateSubjectData(keyPairSuject.getPublic(), name,
                Constants.CERT_TYPE.INTERMEDIATE_CERT);
        IssuerData issuerData = findIssuerByAlias(issuerAlias);
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

    //@TODO: resiti OVDE KAD NIJE LEAF sert za privatni kljuc da se ne prosledjuje Null
    @Override
    public void writeCertificateToKeyStore(X509Certificate cert, Constants.CERT_TYPE certType, PrivateKey pk)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        if (!certType.equals(Constants.CERT_TYPE.LEAF_CERT)) {
            keyStoreRepository.writeKeyEntry(cert.getSerialNumber().toString(), pk, new Certificate[]{cert});
        }
        else {
            keyStoreRepository.writeKeyEntry(cert.getSerialNumber().toString(), null, new Certificate[]{cert});
        }
    }

    /*TODO: impelmentirati da se svi zahtevi za sertifikate prikazu*/
    @Override
    public List<X509Certificate> findAllRequests() {
        return null;
    }

}
