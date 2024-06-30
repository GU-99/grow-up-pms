package com.growup.pms.auth.controller;

import com.growup.pms.auth.dto.AccessTokenResponse;
import com.growup.pms.auth.dto.LoginRequest;
import com.growup.pms.auth.dto.TokenDto;
import com.growup.pms.auth.service.JwtLoginService;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class AuthenticationControllerV1 {
    private final JwtLoginService loginService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        TokenDto tokenDto = loginService.authenticateUser(request);
        setRefreshTokenCookie(response, tokenDto.getRefreshToken());
        return ResponseEntity.ok()
                .body(new AccessTokenResponse(tokenDto.getAccessToken()));
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        // TODO: 서비스가 HTTPS로 배포된 후에 보안 강화를 위해 주석을 해제해야 함
        // refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (tokenProvider.refreshTokenExpirationTime / 1000));
        response.addCookie(refreshTokenCookie);
    }
}
