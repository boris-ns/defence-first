package rs.ac.uns.ftn.siemcentar.configuration;

import org.keycloak.representations.JsonWebToken;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import rs.ac.uns.ftn.siemcentar.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemcentar.service.AuthService;

import java.io.IOException;
import java.util.Arrays;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private AuthService authService;
    private String jwt;

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
