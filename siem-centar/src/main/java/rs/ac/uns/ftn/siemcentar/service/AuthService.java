package rs.ac.uns.ftn.siemcentar.service;

import rs.ac.uns.ftn.siemcentar.dto.response.TokenDTO;

import java.util.Date;

public interface AuthService {

    TokenDTO login();

    TokenDTO login2();

    TokenDTO refreshToken(String refreshToken);

    Date getTokenValid(Long seconds);

    Boolean isTokenValid(Date date);
}
