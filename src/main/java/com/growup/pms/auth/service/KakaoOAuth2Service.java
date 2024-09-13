package com.growup.pms.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growup.pms.auth.service.dto.oauth.kakao.KakaoAccessToken;
import com.growup.pms.auth.service.dto.oauth.kakao.KakaoProfile;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.common.security.jwt.JwtConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuth2Service {

    @Value("${oauth2.kakao.clientId}")
    private String clientId;

    @Value("${oauth2.kakao.redirect-url}")
    private String redirectUrl;

    @Value("${oauth2.kakao.accessToken-request-url}")
    private String accessTokenRequestUrl;

    @Value("${oauth2.kakao.userinfo-request-url}")
    private String userInfoRequestUrl;

    public KakaoAccessToken requestToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper objectMapper = new ObjectMapper();

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("redirect-url", redirectUrl);
        params.add("grant_type", "authorization_code");
        params.add("code", code);

        HttpEntity<LinkedMultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                accessTokenRequestUrl,
                HttpMethod.POST,
                tokenRequest,
                String.class);

        KakaoAccessToken kakaoAccessToken;

        try {
            kakaoAccessToken = objectMapper.readValue(response.getBody(), KakaoAccessToken.class);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.OAUTH2_AUTHENTICATION_FAILED);
        }
        return kakaoAccessToken;
    }

    public KakaoProfile requestProfile(KakaoAccessToken kakaoAccessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper objectMapper = new ObjectMapper();

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", JwtConstants.BEARER_PREFIX + kakaoAccessToken.getAccessToken());

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                userInfoRequestUrl,
                HttpMethod.GET,
                kakaoProfileRequest,
                String.class);

        KakaoProfile kaKaoProfile;

        try {
            kaKaoProfile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.OAUTH2_AUTHENTICATION_FAILED);
        }
        return kaKaoProfile;
    }
}
