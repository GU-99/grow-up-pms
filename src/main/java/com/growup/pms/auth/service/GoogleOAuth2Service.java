package com.growup.pms.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleOAuth2Service extends OAuth2ServiceImpl {

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

    public GoogleOAuth2Service(RestTemplate restTemplate,
                               ObjectMapper objectMapper) {
        super(restTemplate, objectMapper);
    }

    @Override
    protected String getAccessTokenRequestUrl() {
        return accessTokenRequestUrl;
    }

    @Override
    protected String getUserInfoRequestUrl() {
        return userInfoRequestUrl;
    }

    @Override
    protected String getClientId() {
        return clientId;
    }

    @Override
    protected String getClientSecret() {
        return clientSecret;
    }

    @Override
    protected String getRedirectUri() {
        return redirectUri;
    }

    @Override
    protected String getScope() {
        return scope;
    }
}
