package com.growup.pms.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growup.pms.auth.service.dto.oauth.google.GoogleAccessToken;
import com.growup.pms.auth.service.dto.oauth.google.GoogleProfile;
import com.growup.pms.common.security.jwt.JwtConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class GoogleUtil {

    @Value("${oauth2.google.clientId}")
    private String clientId;

    @Value("${oauth2.google.clientSecret}")
    private String clientSecret;

    @Value("${oauth2.google.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.google.scope}")
    private String scope;

    @Value("${oauth2.google.accessToken-request-url}")
    private String accessTokenRequestUrl;

    @Value("${oauth2.google.userinfo-request-url}")
    private String userInfoRequestUrl;

    public GoogleAccessToken requestToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper objectMapper = new ObjectMapper();

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("scope", scope);
        params.add("grant_type", "authorization_code");
        params.add("code", code);

        HttpEntity<LinkedMultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                accessTokenRequestUrl,
                HttpMethod.POST,
                tokenRequest,
                String.class);

        GoogleAccessToken googleAccessToken;

        try {
            googleAccessToken = objectMapper.readValue(response.getBody(), GoogleAccessToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return googleAccessToken;
    }

    public GoogleProfile requestProfile(GoogleAccessToken googleAccessToken) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", JwtConstants.BEARER_PREFIX + googleAccessToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                userInfoRequestUrl,
                HttpMethod.GET,
                request,String.class);

        GoogleProfile googleProfile;

        try {
            googleProfile = objectMapper.readValue(response.getBody(), GoogleProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return googleProfile;
    }
}
