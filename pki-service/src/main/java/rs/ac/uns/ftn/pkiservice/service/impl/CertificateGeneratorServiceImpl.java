package rs.ac.uns.ftn.pkiservice.service.impl;

import org.bouncycastle.asn1.ASN1Boolean;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.cert.crmf.CRMFException;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.x509.extension.X509ExtensionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.pkiservice.configuration.CertificateBuilder;
import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;
import rs.ac.uns.ftn.pkiservice.service.CertificateGeneratorService;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

@Service
public class CertificateGeneratorServiceImpl implements CertificateGeneratorService {

    @Autowired
    private CertificateBuilder certificateBuilder;

    @Override
    public X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, Constants.CERT_TYPE type) {
        try {
            //Posto klasa za generisanje sertifiakta ne moze da primi direktno privatni kljuc pravi se builder za objekat
            //Ovaj objekat sadrzi privatni kljuc izdavaoca sertifikata i koristiti se za potpisivanje sertifikata
            //Parametar koji se prosledjuje je algoritam koji se koristi za potpisivanje sertifiakta
            JcaContentSignerBuilder builder = certificateBuilder.getBuilder();

            //Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za potpisivanje sertifikata
            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

            //Postavljaju se podaci za generisanje sertifiakta
            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
                    new BigInteger(subjectData.getSerialNumber()),
                    subjectData.getStartDate(),
                    subjectData.getEndDate(),
                    subjectData.getX500name(),
                    subjectData.getPublicKey());


            // dodavanje EKSTENZIJA u zavisnosti od TIPA sertifikata
            if(type.equals(Constants.CERT_TYPE.LEAF_CERT)) {
//                certGen.addExtension(Extension.authorityInfoAccess, false, new AuthorityInformationAccess());
//                certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifierStructure(caCert));
//                certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false, new SubjectKeyIdentifierStructure(entityKey));
                certGen.addExtension(Extension.keyUsage, false,
                        new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
                certGen.addExtension(Extension.basicConstraints, false, new BasicConstraints(false));


                KeyPurposeId[] keyPurposeIds = new KeyPurposeId[2];
                keyPurposeIds[0] = KeyPurposeId.id_kp_clientAuth;
                keyPurposeIds[1] = KeyPurposeId.id_kp_serverAuth;

                certGen.addExtension(Extension.extendedKeyUsage , false,
                        new ExtendedKeyUsage(keyPurposeIds));
            }
            else if (type.equals(Constants.CERT_TYPE.SERVER_CERT))
            {
                certGen.addExtension(Extension.basicConstraints, false, new BasicConstraints(false));
                certGen.addExtension(Extension.extendedKeyUsage , false,
                        new ExtendedKeyUsage(KeyPurposeId.id_kp_serverAuth));

                certGen.addExtension(Extension.keyUsage, false,
                        new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));

                byte[] subjectKeyIdentifier = new JcaX509ExtensionUtils()
                        .createSubjectKeyIdentifier(subjectData.getPublicKey()).getKeyIdentifier();
                certGen.addExtension(Extension.subjectKeyIdentifier, false,
                        new SubjectKeyIdentifier(subjectKeyIdentifier));
            }
            else {
                certGen.addExtension(Extension.keyUsage, false,
                        new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));

                certGen.addExtension(Extension.basicConstraints, false, new BasicConstraints(true));


                byte[] subjectKeyIdentifier = new JcaX509ExtensionUtils()
                        .createSubjectKeyIdentifier(subjectData.getPublicKey()).getKeyIdentifier();

                certGen.addExtension(Extension.subjectKeyIdentifier, false,
                        new SubjectKeyIdentifier(subjectKeyIdentifier));
            }


            // ovo je da zna kojim issuera public kljucem da proveri potpis sertifika sertifikate
            // jer su potpisani privatnim, koristi se kad CA ima vise parova kljucava
            // kojima potpisuje sertifikate..
            byte[] authorityKeyIdentifer = new JcaX509ExtensionUtils().
                    createAuthorityKeyIdentifier(issuerData.getPublicKey()).getKeyIdentifier();
//
            certGen.addExtension(Extension.authorityKeyIdentifier, false,
                    new AuthorityKeyIdentifier(authorityKeyIdentifer));

//

            GeneralName altName = new GeneralName(GeneralName.dNSName, "localhost");
            GeneralNames subjectAltName = new GeneralNames(altName);
            certGen.addExtension(Extension.subjectAlternativeName, false, subjectAltName);


            //Generise se sertifikat
            X509CertificateHolder certHolder = certGen.build(contentSigner);

            //Builder generise sertifikat kao objekat klase X509CertificateHolder
            //Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se koristi certConverter
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            //Konvertuje objekat u sertifikat
            return certConverter.getCertificate(certHolder);
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (CertIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IssuerData generateIssuerData(PrivateKey issuerKey, X500Name name, PublicKey publicKey) {
        return new IssuerData(issuerKey, name, publicKey);
    }


    public SubjectData generateSubjectData(PublicKey publicKey, X500Name name, Constants.CERT_TYPE certType) {

        SubjectData subjectData = new SubjectData(publicKey, name, null, null, null);
        generateDate(subjectData, certType);

//        String serialNumber = UUID.randomUUID().toString();
        subjectData.setSerialNumber(String.valueOf(System.currentTimeMillis()));
        //klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku

        //Kreiraju se podaci za sertifikat, sto ukljucuje:
        // - javni kljuc koji se vezuje za sertifikat
        // - podatke o vlasniku
        // - serijski broj sertifikata
        // - od kada do kada vazi sertifikat
        return subjectData;
    }



    @Override
    public PublicKey toPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo)
            throws CRMFException
    {
        Map KEY_ALG_NAMES = new HashMap();
        KEY_ALG_NAMES.put(PKCSObjectIdentifiers.rsaEncryption, "RSA");
        KEY_ALG_NAMES.put(X9ObjectIdentifiers.id_dsa, "DSA");
        KeyFactory keyFactory = null;

        try
        {
            X509EncodedKeySpec xspec = new X509EncodedKeySpec(subjectPublicKeyInfo.getEncoded());
            AlgorithmIdentifier keyAlg = subjectPublicKeyInfo.getAlgorithm();

            String algName = (String)KEY_ALG_NAMES.get(keyAlg.getAlgorithm());
            if(algName != null) {
                keyFactory = KeyFactory.getInstance(algName);
            }
            return keyFactory.generatePublic(xspec);
        }
        catch (Exception e)
        {
            throw new CRMFException("invalid key: " + e.getMessage(), e);
        }
    }

    @Override
    public void generateDate(SubjectData subjectData, Constants.CERT_TYPE certType){
        Calendar calendarLater = Calendar.getInstance();
        calendarLater.setTime(new Date());

        if(certType.equals(Constants.CERT_TYPE.ROOT_CERT)) {
            calendarLater.add(Calendar.YEAR, Constants.ROOT_CERT_DURATION);
        }
        else if(certType.equals((Constants.CERT_TYPE.INTERMEDIATE_CERT))) {
            calendarLater.add(Calendar.YEAR, Constants.INTERMEDIATE_CERT_DURATION);
        }
        else if(certType.equals((Constants.CERT_TYPE.SERVER_CERT))) {
            calendarLater.add(Calendar.YEAR, Constants.SERVER_CERT_DURATION);
        }
        else {
            calendarLater.add(Calendar.YEAR, Constants.LEAF_CERT_DURATION);
        }

        subjectData.setStartDate(new Date());
        subjectData.setEndDate(calendarLater.getTime());
    }

}
