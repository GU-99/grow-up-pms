package com.growup.pms.auth.service;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.auth.dto.LoginDto;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtLoginService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public TokenDto authenticateUser(LoginDto request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityUser principal = (SecurityUser) authentication.getPrincipal();
        TokenDto newToken = tokenProvider.generateToken(principal);
        refreshTokenService.renewRefreshToken(principal.getId(), newToken.getRefreshToken());
        return newToken;
    }
}
