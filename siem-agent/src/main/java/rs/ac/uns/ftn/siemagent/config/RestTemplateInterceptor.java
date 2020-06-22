package rs.ac.uns.ftn.siemagent.config;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import rs.ac.uns.ftn.siemagent.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemagent.service.AuthService;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private AuthService authService;
    private String jwt;
    private TokenDTO token;

    public RestTemplateInterceptor(AuthService authService, String jwt) {
        this.authService = authService;
        this.jwt = jwt;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
                                        ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {

        boolean validToken = authService.isTokenValid(this.jwt);
        if(!validToken) {
            TokenDTO tokenDTO = this.authService.login();
            this.jwt = tokenDTO.getAccesss_token();
        }

        if (!httpRequest.getHeaders().containsKey("Authorization")) {
            httpRequest.getHeaders().add("Authorization", "Bearer " + this.jwt);
        }

        ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, bytes);

        if(response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            TokenDTO tokenDTO = this.authService.login();
            this.jwt = tokenDTO.getAccesss_token();
            httpRequest.getHeaders().replace("Authorization", Arrays.asList("Bearer " + this.jwt));
            response = clientHttpRequestExecution.execute(httpRequest, bytes);
            return response;
        }

        return response;
    }


}
