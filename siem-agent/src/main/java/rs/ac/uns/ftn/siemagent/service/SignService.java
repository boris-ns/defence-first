package rs.ac.uns.ftn.siemagent.service;

import rs.ac.uns.ftn.siemagent.model.Log;

import java.security.InvalidKeyException;
import java.security.PrivateKey;

public interface SignService {

    void singLog(Log l) throws Exception;
}
