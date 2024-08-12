package com.growup.pms.common.security.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtConstants {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
}
