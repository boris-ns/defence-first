package rs.ac.uns.ftn.siemagent.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.siemagent.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemagent.service.AuthService;

import java.io.IOException;

@Configuration
public class RestTemplateConfiguration {

    @Autowired
    private AuthService authService;

    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        TokenDTO token = authService.login();
        restTemplate.getInterceptors().add(new RestTemplateInterceptor(token.getAccesss_token()));
        return restTemplate;
    }
}

class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private String jwt;

    public RestTemplateInterceptor(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
                                        ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {

        if (!httpRequest.getHeaders().containsKey("Authorization")) {
            httpRequest.getHeaders().add("Authorization", "Bearer " + this.jwt);
        }

        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}