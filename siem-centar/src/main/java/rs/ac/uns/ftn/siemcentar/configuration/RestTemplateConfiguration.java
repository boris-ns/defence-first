package rs.ac.uns.ftn.siemcentar.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.siemcentar.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemcentar.service.AuthService;

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
