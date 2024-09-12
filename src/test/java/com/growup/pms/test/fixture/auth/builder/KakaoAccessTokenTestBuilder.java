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
    private String token_type = "bearer";
    private int refresh_token_expires_in = 5183999;
    private int expires_in = 21599;
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
        this.refresh_token_expires_in = 리프레시_토큰_만료_시간;
        return this;
    }

    public KakaoAccessTokenTestBuilder 액세스_토큰_만료_시간이(int 액세스_토큰_만료_시간) {
        this.expires_in = 액세스_토큰_만료_시간;
        return this;
    }

    public KakaoAccessTokenTestBuilder 토큰_유형이(String 토큰_유형) {
        this.token_type = 토큰_유형;
        return this;
    }
    public KakaoAccessTokenTestBuilder 권한_범위가(String 권한_범위) {
        this.scope = 권한_범위;
        return this;
    }

    public KakaoAccessToken 이다() {
        return KakaoAccessToken.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .expires_in(expires_in)
                .refresh_token_expires_in(refresh_token_expires_in)
                .token_type(token_type)
                .scope(scope)
                .build();
    }
}
