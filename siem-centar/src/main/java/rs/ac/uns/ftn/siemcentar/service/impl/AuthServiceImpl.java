package rs.ac.uns.ftn.siemcentar.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.siemcentar.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemcentar.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${agent.username}")
    private String username;

    @Value("${agent.password}")
    private String password;

    @Value("${uri.keycloak.login}")
    private String urlLogin;

    @Override
    public TokenDTO login() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("client_id", "agent");
        map.add("grant_type", "password");
        map.add("username", this.username);
        map.add("password", this.password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<TokenDTO> response = restTemplate.postForEntity(urlLogin, request, TokenDTO.class);

        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            return null;
        }

        return response.getBody();
    }

    @Override
    public TokenDTO refreshToken(String refreshToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("client_id", "agent");
        map.add("grant_type", "password");
        map.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<TokenDTO> response = restTemplate.postForEntity(urlLogin, request, TokenDTO.class);

        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            return null;
        }

        return response.getBody();
    }
}
