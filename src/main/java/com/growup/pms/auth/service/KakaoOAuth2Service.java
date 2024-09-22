package com.growup.pms.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuth2Service extends OAuth2ServiceImpl {

    @Value("${oauth2.kakao.clientId}")
    private String clientId;

    @Value("${oauth2.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.kakao.accessToken-request-uri}")
    private String accessTokenRequestUri;

    @Value("${oauth2.kakao.userinfo-request-uri}")
    private String userInfoRequestUri;

    public KakaoOAuth2Service(RestTemplate restTemplate,
                              ObjectMapper objectMapper) {
        super(restTemplate, objectMapper);
    }

    @Override
    protected String getAccessTokenRequestUri() {
        return accessTokenRequestUri;
    }

    @Override
    protected String getUserInfoRequestUri() {
        return userInfoRequestUri;
    }

    @Override
    protected String getClientId() {
        return clientId;
    }

    @Override
    protected String getClientSecret() {
        return null;
    }

    @Override
    protected String getRedirectUri() {
        return redirectUri;
    }

    @Override
    protected String getScope() {
        return null;
    }
}
