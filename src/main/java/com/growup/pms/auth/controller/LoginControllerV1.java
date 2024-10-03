package com.growup.pms.auth.controller;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.auth.controller.dto.request.LoginRequest;
import com.growup.pms.auth.service.JwtLoginService;
import com.growup.pms.auth.service.RefreshTokenService;
import com.growup.pms.common.aop.annotation.CurrentUser;
import com.growup.pms.common.security.jwt.JwtConstants;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import com.growup.pms.common.util.CookieUtil;
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
        CookieUtil.addCookie(response, JwtConstants.REFRESH_TOKEN_COOKIE_NAME, authTokens.refreshToken(),
                (int) (tokenProvider.refreshTokenExpirationMillis / 1000));
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
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, JwtConstants.BEARER_PREFIX + authTokens.accessToken())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CurrentUser SecurityUser user,
            @CookieValue(JwtConstants.REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
            HttpServletResponse response
    ) {
        redisRefreshTokenService.revoke(user.getId(), refreshToken);
        CookieUtil.removeCookie(response, JwtConstants.REFRESH_TOKEN_COOKIE_NAME);
        return ResponseEntity.ok().build();
    }
}
