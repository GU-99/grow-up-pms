package com.growup.pms.auth.service;

import com.growup.pms.auth.dto.TokenDto;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final JwtTokenProvider tokenProvider;

    public TokenDto generateJwtTokens(Long userId, Authentication authentication) {
        return TokenDto.builder()
                .accessToken(tokenProvider.createAccessToken(userId, authentication))
                .refreshToken(tokenProvider.createRefreshToken(userId, authentication))
                .build();
    }
}
