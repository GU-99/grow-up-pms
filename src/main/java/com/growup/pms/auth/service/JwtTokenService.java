package com.growup.pms.auth.service;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.AuthenticationException;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    public TokenDto refreshJwtTokens(SecurityUser user, String oldRefreshToken) {
        validateRefreshToken(oldRefreshToken);
        TokenDto newToken = tokenProvider.generateToken(user);
        refreshTokenService.renewRefreshToken(user.getId(), newToken.getRefreshToken());
        return newToken;
    }

    private void validateRefreshToken(String token) {
        if (!refreshTokenService.validateToken(token)) {
            throw new AuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN_ERROR);
        }
    }
}
