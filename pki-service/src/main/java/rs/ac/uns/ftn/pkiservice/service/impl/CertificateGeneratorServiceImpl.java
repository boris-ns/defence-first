package rs.ac.uns.ftn.pkiservice.service.impl;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.CertIOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.pkiservice.configuration.CertificateBuilder;
import rs.ac.uns.ftn.pkiservice.constants.Constants;
import rs.ac.uns.ftn.pkiservice.models.IssuerData;
import rs.ac.uns.ftn.pkiservice.models.SubjectData;
import rs.ac.uns.ftn.pkiservice.service.CertificateGeneratorService;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import rs.ac.uns.ftn.pkiservice.service.KeyPairGeneratorService;

import javax.security.auth.x500.X500Principal;

@Service
public class CertificateGeneratorServiceImpl implements CertificateGeneratorService {

    @Autowired
    private CertificateBuilder certificateBuilder;

    @Autowired
    private KeyPairGeneratorService keyPairGeneratorService;


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
//                certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifierStructure(caCert));
//                certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false, new SubjectKeyIdentifierStructure(entityKey));
//                certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(false));
                certGen.addExtension(Extension.keyUsage, true,
                        new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
            }
            else {
                certGen.addExtension(Extension.keyUsage, true,
                        new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));
            }

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
        }
        return null;
    }

    @Override
    public IssuerData generateIssuerData(PrivateKey issuerKey, X500Name name) {
        return new IssuerData(issuerKey, name);
    }


    public SubjectData generateSubjectData(PublicKey publicKey, X500Name name, Constants.CERT_TYPE certType) {

        Calendar calendarLater = Calendar.getInstance();
        calendarLater.setTime(new Date());

        if(certType.equals(Constants.CERT_TYPE.ROOT_CERT)) {
            calendarLater.add(Calendar.YEAR, Constants.ROOT_CERT_DURATION);
        }
        else if(certType.equals((Constants.CERT_TYPE.INTERMEDIATE_CERT))) {
            calendarLater.add(Calendar.YEAR, Constants.INTERMEDIATE_CERT_DURATION);
        }
        else {
            calendarLater.add(Calendar.YEAR, Constants.LEAF_CERT_DURATION);
        }

        Date startDate = new Date();
        Date endDate = calendarLater.getTime();
        String serialNumber = UUID.randomUUID().toString();

        //klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku

        //Kreiraju se podaci za sertifikat, sto ukljucuje:
        // - javni kljuc koji se vezuje za sertifikat
        // - podatke o vlasniku
        // - serijski broj sertifikata
        // - od kada do kada vazi sertifikat
        return new SubjectData(publicKey, name, serialNumber, startDate, endDate);
    }

}
