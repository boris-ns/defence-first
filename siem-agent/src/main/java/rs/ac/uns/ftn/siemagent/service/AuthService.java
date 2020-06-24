package rs.ac.uns.ftn.siemagent.service;

import rs.ac.uns.ftn.siemagent.dto.response.TokenDTO;

import java.util.Date;

public interface AuthService {

    TokenDTO login();

    TokenDTO refreshToken(String refreshToken);

    Date getTokenValid(Long seconds);

    Boolean isTokenValid(Date date);

}
