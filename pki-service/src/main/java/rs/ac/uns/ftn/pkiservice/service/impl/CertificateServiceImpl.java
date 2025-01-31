package rs.ac.uns.ftn.pkiservice.service.impl;

import org.bouncycastle.asn1.x500.X500Name;

import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.exception.exceptions.ResourceNotFoundException;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;
import rs.ac.uns.ftn.pkiservice.repository.KeyStoreRepository;
import rs.ac.uns.ftn.pkiservice.repository.RevokedCertificateRepository;
import rs.ac.uns.ftn.pkiservice.service.CertificateGeneratorService;
import rs.ac.uns.ftn.pkiservice.service.CertificateService;
import rs.ac.uns.ftn.pkiservice.service.KeyPairGeneratorService;

import javax.security.auth.x500.X500PrivateCredential;
import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.*;

import static rs.ac.uns.ftn.pkiservice.constants.Constants.ROOT_ALIAS;

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

    @Value("${generated.certifacates.directory}")
    private String certDirectory;

    @Override
    public Map<Constants.CERT_TYPE, List<X509Certificate>> findAll() {
        List<Certificate> certificateList = keyStoreRepository.readAll();
        Map<Constants.CERT_TYPE, List<X509Certificate>> certificateMap = new HashMap<>();
        Constants.CERT_TYPE type;

        for (Certificate c: certificateList){
            X509Certificate certificate = (X509Certificate) c;

            System.out.println(certificate.getKeyUsage());

            if(certificate.getSerialNumber().toString().equals(ROOT_ALIAS)){
                type = Constants.CERT_TYPE.ROOT_CERT;
            }else if(certificate.getKeyUsage() != null && certificate.getKeyUsage()[5] && certificate.getKeyUsage()[6]){
                type = Constants.CERT_TYPE.INTERMEDIATE_CERT;
            }else {
                type = Constants.CERT_TYPE.LEAF_CERT;
            }
            ArrayList<X509Certificate> list = (ArrayList<X509Certificate>) certificateMap.getOrDefault(type, new ArrayList<>());
            list.add(certificate);
            certificateMap.put(type, list);
        }
        return certificateMap;
    }

    @Override
    public Map<String, Boolean> findAllRevoked() {
        List<String> aliases = keyStoreRepository.readAliases();
        Map<String, Boolean> revokedMap = new HashMap<>();
        for (String alias: aliases) {
            revokedMap.put(alias, ocspRepository.findBySerialNumber(alias).isPresent());
        }
        return revokedMap;
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
        return keyStoreRepository.loadIssuer(alias);
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
    public X509Certificate generateCertificateIntermediate(X500Name subjectName, String issuerAlias)
            throws Exception {
        KeyPair keyPairSuject = keyPairGeneratorService.generateKeyPair();
        SubjectData subjectData = certificateGenerator.generateSubjectData(keyPairSuject.getPublic(), subjectName,
                Constants.CERT_TYPE.INTERMEDIATE_CERT);
        IssuerData issuerData = findIssuerByAlias(issuerAlias);

        //Generise se sertifikat za subjekta, potpisan od strane issuer-a
        X509Certificate cert = certificateGenerator.generateCertificate(subjectData, issuerData,
                Constants.CERT_TYPE.INTERMEDIATE_CERT);

        Certificate[] certificatesChain = createChain(issuerAlias, cert);
        keyStoreRepository.writeKeyEntry(cert.getSerialNumber().toString(), keyPairSuject.getPrivate(), certificatesChain);

        writeCertToFile(cert.getSerialNumber().toString());

        return cert;
    }

    @Override
    public Certificate[] createChain(String issuerAlias, Certificate certificate) {
        Certificate[] certificatesChainIssuer = getCertificateChainByAlias(issuerAlias);

        int size = certificatesChainIssuer.length;
        Certificate[] certificatesChain = new Certificate[size + 1];
        certificatesChain[0] = certificate;


        System.arraycopy(certificatesChainIssuer, 0, certificatesChain, 1, size);
        return certificatesChain;
    }

    @Override
    public void writeCertificateToKeyStore(String alias, Certificate[] certificates, PrivateKey pk)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        keyStoreRepository.writeKeyEntry(alias, pk, certificates);
    }

    @Override
    public void replace(String alias) throws UnrecoverableKeyException,
            CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        X509Certificate certificate = findCertificateByAlias(alias);
        if (alias.equals(ROOT_ALIAS)) {
            replaceRoot(certificate);
        } else if (certificate.getKeyUsage()[5] && certificate.getKeyUsage()[6]) {
            replaceIntermediate(certificate);
        } else {
            throw new ResourceNotFoundException("Certification does not exist or can not be replaced!");
        }
    }

    private void replaceRoot(X509Certificate certificate) throws CertificateException, UnrecoverableKeyException,
            NoSuchAlgorithmException, KeyStoreException, IOException {
        KeyPair keyPair = keyPairGeneratorService.generateKeyPair();
        SubjectData subjectData = new SubjectData(keyPair.getPublic(),
                new JcaX509CertificateHolder(certificate).getSubject(), ROOT_ALIAS, null, null);
        certificateGenerator.generateDate(subjectData, Constants.CERT_TYPE.ROOT_CERT);
        IssuerData issuerData = new IssuerData(keyPair.getPrivate(),
                new JcaX509CertificateHolder(certificate).getIssuer(), keyPair.getPublic());

        Certificate newCertificate = certificateGenerator.generateCertificate(subjectData, issuerData,
                Constants.CERT_TYPE.ROOT_CERT);

        keyStoreRepository.writeKeyEntry(ROOT_ALIAS, keyPair.getPrivate(), new Certificate[]{newCertificate});
        childReplace(issuerData, newCertificate, 1);
    }

    private void replaceIntermediate(X509Certificate certificate) throws CertificateException, NoSuchAlgorithmException,
            KeyStoreException, IOException, UnrecoverableKeyException {
        String alias = certificate.getSerialNumber().toString();
        Certificate[] certificates = keyStoreRepository.readCertificateChain(alias);
        KeyPair keyPair = keyPairGeneratorService.generateKeyPair();
        SubjectData subjectData = new SubjectData(keyPair.getPublic(),
                new JcaX509CertificateHolder(certificate).getSubject(), alias, null, null);
        certificateGenerator.generateDate(subjectData, Constants.CERT_TYPE.INTERMEDIATE_CERT);
        IssuerData issuerData = findIssuerByAlias(((X509Certificate) certificates[1]).getSerialNumber().toString());

        Certificate newCertificate = certificateGenerator.generateCertificate(subjectData, issuerData,
                Constants.CERT_TYPE.INTERMEDIATE_CERT);
        certificates[0] = newCertificate;
        keyStoreRepository.writeKeyEntry(alias, keyPair.getPrivate(), certificates);
        IssuerData id = new IssuerData(keyPair.getPrivate(), subjectData.getX500name(), keyPair.getPublic());
        childReplace(id, newCertificate, certificates.length);
    }

    private void childReplace(IssuerData issuerData, Certificate newCertificate, int size) throws
            CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String alias = ((X509Certificate) newCertificate).getSerialNumber().toString();
        List<Certificate[]> listChain = keyStoreRepository.listChain();
        for (Certificate[] chain : listChain) {
            if (chain.length == size + 1 &&
                    ((X509Certificate) chain[chain.length - size]).getSerialNumber().toString().equals(alias)) {
                X509Certificate cert = (X509Certificate) chain[0];
                SubjectData sd = new SubjectData(cert.getPublicKey(), new JcaX509CertificateHolder(cert).getSubject(),
                        cert.getSerialNumber().toString(), cert.getNotBefore(), cert.getNotAfter());
                Constants.CERT_TYPE type;
                if (cert.getKeyUsage()[5] && cert.getKeyUsage()[6]) {
                    type = Constants.CERT_TYPE.INTERMEDIATE_CERT;
                } else {
                    type = Constants.CERT_TYPE.LEAF_CERT;
                }
                Certificate newCert = certificateGenerator.generateCertificate(sd, issuerData, type);
                chain[0] = newCert;
                chain[1] = newCertificate;
                keyStoreRepository.writeKeyEntry(cert.getSerialNumber().toString(),
                        keyStoreRepository.readPrivateKey(cert.getSerialNumber().toString()), chain);
            }
        }

        for (Certificate[] chain : listChain) {
            if (chain.length > size + 1 &&
                    ((X509Certificate) chain[chain.length - size]).getSerialNumber().toString().equals(alias)) {
                String certAlias = ((X509Certificate) chain[0]).getSerialNumber().toString();
                String issuerAlias = ((X509Certificate) chain[chain.length - (size + 1)]).getSerialNumber().toString();
                Certificate[] chainIssuer = keyStoreRepository.readCertificateChain(issuerAlias);
                chain[chain.length - (size + 1)] = chainIssuer[0];
                chain[chain.length - size] = chainIssuer[1];
                keyStoreRepository.writeKeyEntry(certAlias, keyStoreRepository.readPrivateKey(certAlias), chain);
            }
        }
    }


    @Override
    public void writeCertToFile(String serialNumber) throws Exception {


        Certificate[] chain = keyStoreRepository.readCertificateChain(serialNumber);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        CertPath pathC = cf.generateCertPath(Arrays.asList(chain));


        StringWriter sw = new StringWriter();
        JcaPEMWriter pm = new JcaPEMWriter(sw);
        for(Certificate certificate : pathC.getCertificates()) {
            pm.writeObject(certificate);
            pm.flush();
            sw.append(",");
        }
        pm.close();

        String fileName = "cert_" + serialNumber + ".crt";
        String path = certDirectory + "/" + fileName;


        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(sw.toString());
            writer.close();
        }

    }
}
