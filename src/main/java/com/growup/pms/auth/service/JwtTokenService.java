package com.growup.pms.auth.service;

import com.growup.pms.auth.dto.TokenDto;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.AuthenticationException;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    public TokenDto generateJwtTokens(Long userId, Authentication authentication) {
        return TokenDto.builder()
                .accessToken(tokenProvider.createAccessToken(userId, authentication))
                .refreshToken(tokenProvider.createRefreshToken(userId, authentication))
                .build();
    }

    public TokenDto refreshJwtTokens(String refreshToken) {
        validateRefreshToken(refreshToken);
        Long userId = tokenProvider.getUserIdFromToken(refreshToken);
        refreshTokenService.updateRefreshToken(userId, refreshToken);
        return generateJwtTokens(userId, tokenProvider.getAuthentication(refreshToken));
    }

    private void validateRefreshToken(String token) {
        if (!refreshTokenService.validateToken(token)) {
            throw new AuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN_ERROR);
        }
    }
}
