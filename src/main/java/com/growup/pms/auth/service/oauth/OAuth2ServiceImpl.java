package com.growup.pms.auth.service.oauth;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growup.pms.auth.service.dto.oauth.OAuthAccessToken;
import com.growup.pms.auth.service.dto.oauth.OAuthProfile;
import com.growup.pms.auth.service.dto.oauth.google.GoogleAccessToken;
import com.growup.pms.auth.service.dto.oauth.google.GoogleProfile;
import com.growup.pms.auth.service.dto.oauth.kakao.KakaoAccessToken;
import com.growup.pms.auth.service.dto.oauth.kakao.KakaoProfile;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.common.security.jwt.JwtConstants;
import com.growup.pms.user.domain.Provider;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public abstract class OAuth2ServiceImpl implements OAuth2Service {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String AUTHORIZATION_CODE = "authorization_code";

    protected abstract String getAccessTokenRequestUri();

    protected abstract String getUserInfoRequestUri();

    protected abstract String getClientId();

    protected abstract String getClientSecret();

    protected abstract String getRedirectUri();

    protected abstract String getScope();

    @Override
    public OAuthAccessToken requestToken(Provider provider, String code) {
        HttpHeaders headers = createHeaders();
        LinkedMultiValueMap<String, String> params = createTokenRequestParams(code);

        HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        return (OAuthAccessToken) sendRequest(getAccessTokenRequestUri(), HttpMethod.POST,
                request, getAccessTokenClass(provider));
    }

    @Override
    public OAuthProfile requestProfile(Provider provider, OAuthAccessToken accessToken) {
        HttpHeaders headers = createHeaders();
        headers.add(AUTHORIZATION, JwtConstants.BEARER_PREFIX + accessToken.getAccessToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        return (OAuthProfile) sendRequest(getUserInfoRequestUri(), HttpMethod.GET, request, getProfileClass(provider));
    }

    @NotNull
    private LinkedMultiValueMap<String, String> createTokenRequestParams(String code) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", getClientId());
        params.add("redirect_uri", getRedirectUri());
        params.add("grant_type", AUTHORIZATION_CODE);
        params.add("code", code);
        Optional.ofNullable(getScope()).ifPresent(scope -> params.add("scope", scope));
        Optional.ofNullable(getClientSecret()).ifPresent(secret -> params.add("client_secret", secret));
        return params;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, APPLICATION_FORM_URLENCODED);
        return headers;
    }

    @NotNull
    private static Class<?> getClassForProvider(Provider provider, Class<?> kakaoClass, Class<?> googleClass) {
        return switch (provider) {
            case KAKAO -> kakaoClass;
            case GOOGLE -> googleClass;
            default -> throw new BusinessException(ErrorCode.INVALID_PROVIDER);
        };
    }

    @NotNull
    private static Class<?> getAccessTokenClass(Provider provider) {
        return getClassForProvider(provider, KakaoAccessToken.class, GoogleAccessToken.class);
    }

    @NotNull
    private static Class<?> getProfileClass(Provider provider) {
        return getClassForProvider(provider, KakaoProfile.class, GoogleProfile.class);
    }

    private <T> T sendRequest(String url, HttpMethod method, HttpEntity<?> request, Class<T> responseType) {
        ResponseEntity<String> response = restTemplate.exchange(url, method, request, String.class);
        try {
            return objectMapper.readValue(response.getBody(), responseType);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.OAUTH2_AUTHENTICATION_FAILED);
        }
    }
}
