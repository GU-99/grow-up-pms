package com.growup.pms.auth.controller;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.auth.controller.dto.request.LoginRequest;
import com.growup.pms.auth.service.JwtLoginService;
import com.growup.pms.auth.service.RefreshTokenService;
import com.growup.pms.common.aop.annotation.CurrentUser;
import com.growup.pms.common.security.jwt.JwtConstants;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class LoginControllerV1 {
    
    private final RefreshTokenService redisRefreshTokenService;
    private final JwtLoginService loginService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        TokenResponse authTokens = loginService.authenticateUser(request.toCommand());

        setRefreshTokenCookie(response, authTokens.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, JwtConstants.BEARER_PREFIX + authTokens.accessToken())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(
            @CookieValue(JwtConstants.REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
            HttpServletResponse response
    ) {
        TokenResponse authTokens = redisRefreshTokenService.refreshJwtTokens(refreshToken);

        setRefreshTokenCookie(response, authTokens.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, JwtConstants.BEARER_PREFIX + authTokens.accessToken())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CurrentUser SecurityUser user,
            @CookieValue(JwtConstants.REFRESH_TOKEN_COOKIE_NAME) String refreshToken
    ) {
        redisRefreshTokenService.revoke(user.getId(), refreshToken);
        return ResponseEntity.ok().build();
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
