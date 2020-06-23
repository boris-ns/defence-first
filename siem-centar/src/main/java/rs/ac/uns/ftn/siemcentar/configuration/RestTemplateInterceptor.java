package rs.ac.uns.ftn.siemcentar.configuration;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import rs.ac.uns.ftn.siemcentar.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemcentar.service.AuthService;

import java.io.IOException;
import java.util.Date;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private AuthService authService;
    private String jwt;
    private String jwtRefresh;
    private Date expiresIn;
    private Date expiresInRefresh;

    public RestTemplateInterceptor(AuthService authService, TokenDTO token) {
        this.authService = authService;
        setData(token);
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
                                        ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        if(!authService.isTokenValid(this.expiresIn)) {
            TokenDTO token;
            if (!authService.isTokenValid(this.expiresInRefresh)){
                token = this.authService.login();
            } else {
                token = this.authService.refreshToken(this.jwtRefresh);
            }
            setData(token);
        }

        if (!httpRequest.getHeaders().containsKey("Authorization")) {
            httpRequest.getHeaders().add("Authorization", "Bearer " +
                    this.jwt);
        }

        ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, bytes);
        return response;
    }

    private void setData(TokenDTO token) {
        this.jwt = token.getAccesss_token();
        this.jwtRefresh = token.getRefresh_token();
        this.expiresIn = this.authService.getTokenValid(token.getExpires_in());
        this.expiresInRefresh = this.authService.getTokenValid(token.getRefresh_expires_in());
    }
}
