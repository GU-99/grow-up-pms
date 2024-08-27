package com.growup.pms.auth.service;

import com.growup.pms.common.security.jwt.dto.TokenResponse;

public interface RefreshTokenService {
    void save(Long userId, String token);

    TokenResponse refreshJwtTokens(String oldRefreshToken);

    void revoke(Long userId, String refreshToken);
}
