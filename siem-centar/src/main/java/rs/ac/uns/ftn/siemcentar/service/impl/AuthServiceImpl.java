package rs.ac.uns.ftn.siemcentar.service.impl;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.siemcentar.dto.response.TokenDTO;
import rs.ac.uns.ftn.siemcentar.service.AuthService;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

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

    @Override
    public Boolean isTokenValid(String token) {
        Boolean retVal = false;
        int indxStartBody = token.indexOf(".");
        int indxEndBody = token.indexOf(".", indxStartBody+1);
        Base64 base64 = new Base64(true);
        String decodedBody = new String(base64.decode(token.substring(indxStartBody, indxEndBody+1)));

        int startIndx = decodedBody.indexOf("exp");
        int endIndx = decodedBody.indexOf(",", startIndx);
        String expDate = decodedBody.substring(startIndx, endIndx);

        String date = expDate.split(":")[1];
        Instant instant = Instant.ofEpochSecond(Long.parseLong(date));
        Date d = Date.from(instant);

        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MINUTE, -1);
        d = c.getTime();

        if(d.after(new Date())) {
            retVal = true;
        }
        return retVal;
    }
}
