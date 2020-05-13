package rs.ac.uns.ftn.siemcentar.service;

import rs.ac.uns.ftn.siemcentar.dto.response.TokenDTO;

public interface AuthService {

    TokenDTO login();
    TokenDTO refreshToken(String refreshToken);
}
