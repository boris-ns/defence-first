package rs.ac.uns.ftn.pkiservice.service;

import rs.ac.uns.ftn.pkiservice.models.Certificate;

public interface CertificateService {

    Certificate findById(String id);
}
