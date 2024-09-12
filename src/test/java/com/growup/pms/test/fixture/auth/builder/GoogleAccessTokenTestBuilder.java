package com.growup.pms.test.fixture.auth.builder;

import com.growup.pms.auth.service.dto.oauth.google.GoogleAccessToken;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GoogleAccessTokenTestBuilder {

    private String accessToken = "액세스 토큰";
    private String expiresIn = "3599";
    private String refreshToken = "리프레시 토큰";
    private String scope = "https://www.googleapis.com/auth/analytics.readonly";
    private String tokenType = "Bearer";
    private String idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImQ3YjkzOTc3MWE3ODAwYzQxM2Y5MDA1MTAxMmQ5NzU5ODE5MTZk";

    public static GoogleAccessTokenTestBuilder 구글_액세스_토큰은() {
        return new GoogleAccessTokenTestBuilder();
    }

    public GoogleAccessTokenTestBuilder 액세스_토큰이(String 액세스_토큰) {
        this.accessToken = 액세스_토큰;
        return this;
    }

    public GoogleAccessTokenTestBuilder 리프레시_토큰이(String 리프레시_토큰) {
        this.refreshToken = 리프레시_토큰;
        return this;
    }

    public GoogleAccessTokenTestBuilder 액세스_토큰_만료_시간이(String 액세스_토큰_만료_시간) {
        this.expiresIn = 액세스_토큰_만료_시간;
        return this;
    }

    public GoogleAccessTokenTestBuilder 토큰_유형이(String 토큰_유형) {
        this.tokenType = 토큰_유형;
        return this;
    }

    public GoogleAccessTokenTestBuilder 권한_범위가(String 권한_범위) {
        this.scope = 권한_범위;
        return this;
    }

    public GoogleAccessTokenTestBuilder 아이디_토큰이(String 아이디토큰) {
        this.idToken = 아이디토큰;
        return this;
    }

    public GoogleAccessToken 이다() {
        return GoogleAccessToken.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .id_token(idToken)
                .expires_in(expiresIn)
                .token_type(tokenType)
                .scope(scope)
                .build();
    }
}
