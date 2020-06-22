package rs.ac.uns.ftn.siemagent.config;

import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import rs.ac.uns.ftn.siemagent.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemagent.service.AuthService;

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
