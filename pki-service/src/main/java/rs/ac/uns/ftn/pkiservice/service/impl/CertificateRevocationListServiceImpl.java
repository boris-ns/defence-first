package rs.ac.uns.ftn.pkiservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.pkiservice.exception.exceptions.ApiRequestException;
import rs.ac.uns.ftn.pkiservice.repository.CertificateRevocationListRepository;
import rs.ac.uns.ftn.pkiservice.service.CertificateRevocationListService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CertificateRevocationListServiceImpl implements CertificateRevocationListService {

    @Autowired
    private CertificateRevocationListRepository crlRepository;

    @Override
    public boolean checkIsRevoked(String id) {
        return crlRepository.findByCertificateId(id).isPresent();
    }

    @Override
    public void addCrlItem(String certificationId) {
        if (this.checkIsRevoked(certificationId)) {
            throw new ApiRequestException("This certification is already revoked.");
        }

//        CertificateRevocationItem cri = new CertificateRevocationItem(certificationId, new Date());
//        List<CertificateRevocationList> result = crlRepository.findAll();
//        CertificateRevocationList crl = null;
//
//        if (result.isEmpty()) {
//            crl = new CertificateRevocationList();
//            crl.setRevokedCertifications(new ArrayList<>());
//        } else {
//            // @HACK: We know that only 1 CRL exists in the database
//            crl = result.get(0);
//        }
//
//        crl.getRevokedCertifications().add(cri);
//        crlRepository.save(crl);
    }
}
