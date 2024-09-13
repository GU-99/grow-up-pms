package com.growup.pms.auth.service.dto.oauth.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonNaming(value = SnakeCaseStrategy.class)
public class KakaoAccessToken {
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private String scope;
    private int expiresIn;
    private int refreshTokenExpiresIn;

    @Builder
    public KakaoAccessToken(String accessToken, String tokenType, String refreshToken, String scope, int expiresIn,
                            int refreshTokenExpiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.scope = scope;
        this.expiresIn = expiresIn;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }
}
