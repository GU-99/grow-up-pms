package com.growup.pms.auth.controller;

import com.growup.pms.auth.service.oauth.OauthLoginService;
import com.growup.pms.common.security.jwt.JwtConstants;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import com.growup.pms.common.util.CookieUtil;
import com.growup.pms.user.domain.Provider;
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
public class OauthLoginControllerV1 {

    private final OauthLoginService oauthLoginService;
    private final JwtTokenProvider tokenProvider;

    @GetMapping("/{provider}")
    public ResponseEntity<Void> login(
            @PathVariable String provider,
            @RequestParam("code") String code,
            HttpServletResponse response
    ) {
        Provider providerEnum = Provider.valueOf(provider.toUpperCase());
        TokenResponse authTokens = oauthLoginService.authenticate(providerEnum, code);
        CookieUtil.addCookie(response, JwtConstants.REFRESH_TOKEN_COOKIE_NAME, authTokens.refreshToken(),
                (int) (tokenProvider.refreshTokenExpirationMillis / 1000));
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, JwtConstants.BEARER_PREFIX + authTokens.accessToken())
                .build();
    }
}
