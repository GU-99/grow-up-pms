package com.growup.pms.auth.service;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.auth.dto.SignInRequest;
import com.growup.pms.auth.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtLoginService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenService jwtTokenService;

    public TokenDto authenticateUser(SignInRequest request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        return jwtTokenService.generateJwtTokens(((SecurityUser) authentication.getPrincipal()).getId(), authentication);
    }
}
