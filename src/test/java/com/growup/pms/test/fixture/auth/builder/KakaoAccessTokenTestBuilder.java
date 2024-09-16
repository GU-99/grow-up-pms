package com.growup.pms.test.fixture.auth.builder;

import com.growup.pms.auth.service.dto.oauth.kakao.KakaoAccessToken;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoAccessTokenTestBuilder {
    private String accessToken = "액세스 토큰";
    private String refreshToken = "리프레시 토큰";
    private String tokenType = "bearer";
    private int refreshTokenExpiresIn = 5183999;
    private int expiresIn = 21599;
    private String scope = "account_email profile_image profile_nickname";

    public static KakaoAccessTokenTestBuilder 카카오_액세스_토큰은() {
        return new KakaoAccessTokenTestBuilder();
    }

    public KakaoAccessTokenTestBuilder 액세스_토큰이(String 액세스_토큰) {
        this.accessToken = 액세스_토큰;
        return this;
    }

    public KakaoAccessTokenTestBuilder 리프레시_토큰이(String 리프레시_토큰) {
        this.refreshToken = 리프레시_토큰;
        return this;
    }

    public KakaoAccessTokenTestBuilder 리프레시_토큰_만료_시간이(int 리프레시_토큰_만료_시간) {
        this.refreshTokenExpiresIn = 리프레시_토큰_만료_시간;
        return this;
    }

    public KakaoAccessTokenTestBuilder 액세스_토큰_만료_시간이(int 액세스_토큰_만료_시간) {
        this.expiresIn = 액세스_토큰_만료_시간;
        return this;
    }

    public KakaoAccessTokenTestBuilder 토큰_유형이(String 토큰_유형) {
        this.tokenType = 토큰_유형;
        return this;
    }
    public KakaoAccessTokenTestBuilder 권한_범위가(String 권한_범위) {
        this.scope = 권한_범위;
        return this;
    }

    public KakaoAccessToken 이다() {
        return KakaoAccessToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(expiresIn)
                .refreshTokenExpiresIn(refreshTokenExpiresIn)
                .tokenType(tokenType)
                .scope(scope)
                .build();
    }
}
