package com.growup.pms.test.fixture.auth.builder;

import com.growup.pms.common.security.jwt.dto.TokenResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenResponseTestBuilder {
    private String accessToken = "액세스 토큰";
    private String refreshToken = "리프레시 토큰";

    public static TokenResponseTestBuilder 발급된_토큰은() {
        return new TokenResponseTestBuilder();
    }

    public TokenResponseTestBuilder 액세스_토큰이(String 액세스_토큰) {
        this.accessToken = 액세스_토큰;
        return this;
    }

    public TokenResponseTestBuilder 리프레시_토큰이(String 리프레시_토큰) {
        this.refreshToken = 리프레시_토큰;
        return this;
    }

    public TokenResponse 이다() {
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
