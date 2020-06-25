package rs.ac.uns.ftn.siemcentar.service.impl;

import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.siemcentar.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemcentar.service.AuthService;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${agent.username}")
    private String username;

    @Value("${agent.password}")
    private String password;

    @Value("${uri.keycloak.login}")
    private String urlLogin;

    @Autowired
    @Qualifier("httFactoryNoOCSP")
    private HttpComponentsClientHttpRequestFactory requestFactory;

    @Override
    public TokenDTO login() {
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("client_id", "agent");
        map.add("grant_type", "password");
        map.add("username", this.username);
        map.add("password", this.password);

        return sendRequest(map);
    }


    @Override
    public TokenDTO login2() {


        HashMap<String, Object> map = new HashMap<>();
        map.put("secret", "a66b5002-a23c-4028-bd2d-f44995919ac1");



        Configuration configuration = new Configuration();
        configuration.setRealm("DefenceFirst");
        configuration.setAuthServerUrl("http://localhost:8080/auth");
        configuration.setResource("siem-centar");
        configuration.setCredentials(map);
//        configuration.setTruststore("src/main/resources/keystore/trustStore.jks");
//        configuration.setTruststorePassword("superstrongpassword");
//        configuration.setDisableTrustManager(true);

        AuthzClient authzClient = AuthzClient.create(configuration);


        AuthorizationRequest request = new AuthorizationRequest();
        AuthorizationResponse response = authzClient.authorization().authorize(request);
        String token = response.getToken();

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAccess_token(response.getToken());
        tokenDTO.setRefresh_token(response.getRefreshToken());
        tokenDTO.setExpires_in(response.getExpiresIn());
        tokenDTO.setRefresh_expires_in(response.getRefreshExpiresIn());

        System.out.print(token);

        return tokenDTO;



    }


    @Override
    public TokenDTO refreshToken(String refreshToken) {
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("client_id", "agent");
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", refreshToken);

        return sendRequest(map);
    }

    @Override
    public Date getTokenValid(Long seconds) {
        Instant instant = (new Date()).toInstant().plusSeconds(seconds - 10);
        return Date.from(instant);
    }

    @Override
    public Boolean isTokenValid(Date date) {
        if(date.after(new Date())) {
            return true;
        }
        return false;
    }


    private TokenDTO sendRequest(MultiValueMap<String, String> map) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<TokenDTO> response = restTemplate.postForEntity(urlLogin, request, TokenDTO.class);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            return null;
        }

        return response.getBody();
    }
}
