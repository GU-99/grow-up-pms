package com.growup.pms.auth.service;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.auth.service.dto.LoginCommand;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtLoginService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService redisRefreshTokenService;

    @Transactional
    public TokenResponse authenticateUser(LoginCommand command) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(command.username(), command.password());
        Authentication authentication = authenticate(authenticationToken);
        SecurityUser principal = (SecurityUser) authentication.getPrincipal();
        TokenResponse newToken = tokenProvider.generateToken(principal);

        redisRefreshTokenService.save(principal.getId(), newToken.refreshToken());
        return newToken;
    }

    private Authentication authenticate(UsernamePasswordAuthenticationToken authenticationToken) {
        try {
            return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (AuthenticationException ex) {
            throw new BusinessException(ErrorCode.AUTHENTICATION_FAILED);
        }
    }
}
