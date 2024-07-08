package com.growup.pms.test.fixture.auth;

import com.growup.pms.common.security.jwt.dto.TokenDto;

public class TokenDtoFixture {

    public static final String VALID_ACCESS_TOKEN = "validAccessToken";
    public static final String VALID_REFRESH_TOKEN = "validRefreshToken";

    public static final String INVALID_ACCESS_TOKEN = "invalidAccessToken";
    public static final String INVALID_REFRESH_TOKEN = "invalidRefreshToken";

    public static final String NEW_ACCESS_TOKEN = "newAccessToken";
    public static final String NEW_REFRESH_TOKEN = "newRefreshToken";

    public static TokenDto createDefaultDto() {
        return createDefaultDtoBuilder().build();
    }

    public static TokenDto.TokenDtoBuilder createDefaultDtoBuilder() {
        return TokenDto.builder()
                .accessToken(VALID_ACCESS_TOKEN)
                .refreshToken(VALID_REFRESH_TOKEN);
    }
}
