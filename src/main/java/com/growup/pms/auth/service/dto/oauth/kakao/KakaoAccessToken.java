package com.growup.pms.auth.service.dto.oauth.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growup.pms.auth.service.dto.oauth.OauthAccessToken;
import lombok.Builder;

@Builder
@JsonNaming(value = SnakeCaseStrategy.class)
public record KakaoAccessToken(
        String accessToken,
        String tokenType,
        String refreshToken,
        String scope,
        int expiresIn,
        int refreshTokenExpiresIn
) implements OauthAccessToken {

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public String getTokenType() {
        return tokenType;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public int getExpiresIn() {
        return expiresIn;
    }
}
