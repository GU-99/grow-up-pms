package com.growup.pms.auth.service;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtTokenService {
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public TokenResponse refreshJwtTokens(String oldRefreshToken) {
        validateRefreshToken(oldRefreshToken);

        SecurityUser currentUser = (SecurityUser) tokenProvider.getAuthentication(oldRefreshToken).getPrincipal();
        TokenResponse newToken = tokenProvider.generateToken(currentUser);

        refreshTokenService.renewRefreshToken(currentUser.getId(), newToken.refreshToken());
        return newToken;
    }

    private void validateRefreshToken(String token) {
        if (!StringUtils.hasText(token) || !refreshTokenService.validateToken(token)) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN_ERROR);
        }
    }
}
