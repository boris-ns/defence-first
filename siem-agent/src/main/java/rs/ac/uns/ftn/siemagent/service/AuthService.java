package rs.ac.uns.ftn.siemagent.service;

import rs.ac.uns.ftn.siemagent.dto.response.TokenDTO;

public interface AuthService {

    TokenDTO login();
    TokenDTO refreshToken(String refreshToken);
    Boolean isTokenValid(String token);
}
