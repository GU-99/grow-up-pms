package com.growup.pms.auth.controller;

import com.growup.pms.auth.service.OAuthLoginService;
import com.growup.pms.common.security.jwt.JwtConstants;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/login")
@RequiredArgsConstructor
public class OAuthLoginControllerV1 {

    private final OAuthLoginService oAuthLoginService;
    private final JwtTokenProvider tokenProvider;

    @GetMapping("/{provider}")
    public ResponseEntity<Void> login(@PathVariable String provider, @RequestParam("code") String code,
                                      HttpServletResponse response) {
        TokenResponse authTokens = oAuthLoginService.authenticate(provider, code);
        setRefreshTokenCookie(response, authTokens.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, JwtConstants.BEARER_PREFIX + authTokens.accessToken())
                .build();
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie(JwtConstants.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        // TODO: 서비스가 HTTPS로 배포된 후에 보안 강화를 위해 주석을 해제해야 함
        // refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (tokenProvider.refreshTokenExpirationMillis / 1000));
        response.addCookie(refreshTokenCookie);
    }
}
