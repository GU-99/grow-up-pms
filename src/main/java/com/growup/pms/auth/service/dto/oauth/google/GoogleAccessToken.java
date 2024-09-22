package com.growup.pms.auth.service.dto.oauth.google;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growup.pms.auth.service.dto.oauth.OAuthAccessToken;
import lombok.Builder;

@Builder
@JsonNaming(SnakeCaseStrategy.class)
public record GoogleAccessToken(
        String accessToken,
        String expiresIn,
        String refreshToken,
        String scope,
        String tokenType,
        String idToken
) implements OAuthAccessToken {

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
        return Integer.parseInt(expiresIn);
    }
}
