package com.growup.pms.auth.service;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.auth.service.dto.LoginCommand;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
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
    public TokenResponse authenticateUser(LoginCommand command) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(command.username(), command.password());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityUser principal = (SecurityUser) authentication.getPrincipal();
        TokenResponse newToken = tokenProvider.generateToken(principal);

        refreshTokenService.renewRefreshToken(principal.getId(), newToken.refreshToken());
        return newToken;
    }
}
