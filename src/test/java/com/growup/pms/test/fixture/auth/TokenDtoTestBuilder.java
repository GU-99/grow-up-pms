package com.growup.pms.test.fixture.auth;

import com.growup.pms.common.security.jwt.dto.TokenDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenDtoTestBuilder {
    private String accessToken = "액세스 토큰";
    private String refreshToken = "리프레시 토큰";

    public static TokenDtoTestBuilder 발급된_토큰은() {
        return new TokenDtoTestBuilder();
    }

    public TokenDtoTestBuilder 액세스_토큰이(String 액세스_토큰) {
        this.accessToken = 액세스_토큰;
        return this;
    }

    public TokenDtoTestBuilder 리프레시_토큰이(String 리프레시_토큰) {
        this.refreshToken = 리프레시_토큰;
        return this;
    }

    public TokenDto 이다() {
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
