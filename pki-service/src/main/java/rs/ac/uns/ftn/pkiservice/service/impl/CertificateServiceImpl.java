package rs.ac.uns.ftn.pkiservice.service.impl;

import org.bouncycastle.asn1.x500.X500Name;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.dto.response.SimpleCertificateDTO;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;
import rs.ac.uns.ftn.pkiservice.repository.KeyStoreRepository;
import rs.ac.uns.ftn.pkiservice.repository.RevokedCertificateRepository;
import rs.ac.uns.ftn.pkiservice.service.CertificateGeneratorService;
import rs.ac.uns.ftn.pkiservice.service.CertificateService;
import rs.ac.uns.ftn.pkiservice.service.KeyPairGeneratorService;

import javax.security.auth.x500.X500PrivateCredential;
import java.io.IOException;
import java.math.BigInteger;
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

    @Autowired
    private RevokedCertificateRepository ocspRepository;

    @Override
    public List<X509Certificate> findAll() {
        List<Certificate> certificateList = keyStoreRepository.readAll();
        return certificateList.stream().map(X509Certificate.class::cast).collect(Collectors.toList());
    }

    @Override
    public List<SimpleCertificateDTO> findAllDto() {
        List<X509Certificate> certificates = this.findAll();

        return certificates.stream()
                .map(cert -> {
                    boolean revoked = ocspRepository.findBySerialNumber(cert.getSerialNumber().toString()).isPresent();
                    return new SimpleCertificateDTO(cert, revoked);
                })
                .collect(Collectors.toList());
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

        //Generise se sertifikat za subjekta, potpisan od strane issuer-a
        X509Certificate cert = certificateGenerator.generateCertificate(subjectData, issuerData,
                Constants.CERT_TYPE.INTERMEDIATE_CERT);

        Certificate[] certificatesChain = createChain(issuerAlias, cert);
        keyStoreRepository.writeKeyEntry(cert.getSerialNumber().toString(), keyPairSuject.getPrivate(), certificatesChain);
        return cert;
    }

    @Override
    public Certificate[] createChain(String issuerAlias, Certificate certificate){
        Certificate[] certificatesChainIssuer = getCertificateChainByAlias(issuerAlias);

        int size = certificatesChainIssuer.length;
        Certificate[] certificatesChain = new Certificate[size + 1];
        certificatesChain[0] = certificate;
        for (int i = 0; i < size; i++) {
            certificatesChain[i + 1] = certificatesChainIssuer[i];
        }
        return certificatesChain;
    }

    @Override
    public void writeCertificateToKeyStore(String alias, Certificate[] certificates, PrivateKey pk)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        keyStoreRepository.writeKeyEntry(alias, pk, certificates);
    }

    @Override
    public void replace(String id) {

    }

}
