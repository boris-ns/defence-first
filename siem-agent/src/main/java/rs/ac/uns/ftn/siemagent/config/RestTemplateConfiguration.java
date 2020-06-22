package rs.ac.uns.ftn.siemagent.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.siemagent.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemagent.service.AuthService;

@Configuration
public class RestTemplateConfiguration {

    @Autowired
    private AuthService authService;

    @Autowired @Qualifier("httFactory")
    private HttpComponentsClientHttpRequestFactory requestFactory;

    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        TokenDTO token = authService.login();
        restTemplate.getInterceptors().add(new RestTemplateInterceptor(authService, token.getAccesss_token()));

        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
}
