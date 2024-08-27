package com.growup.pms.auth.service;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import com.growup.pms.common.util.HashingUtil;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenService implements RefreshTokenService {
    private static final String REDIS_KEYSPACE_REFRESH_TOKEN = "user:%s:refresh-token:%s";

    private final JwtTokenProvider tokenProvider;
    private final StringRedisTemplate stringRedisTemplate;

    public void save(Long userId, String token) {
        String key = generateRedisKey(userId, token);
        setRefreshTokenWithExpiration(key);
    }

    public void revoke(Long userId, String refreshToken) {
        stringRedisTemplate.delete(generateRedisKey(userId, refreshToken));
    }

    public TokenResponse refreshJwtTokens(String oldRefreshToken) {
        validateRefreshTokenFormat(oldRefreshToken);

        SecurityUser currentUser = (SecurityUser) tokenProvider.getAuthentication(oldRefreshToken).getPrincipal();
        TokenResponse newToken = tokenProvider.generateToken(currentUser);

        renew(currentUser.getId(), oldRefreshToken, newToken.refreshToken());
        return newToken;
    }

    private void renew(Long userId, String oldRefreshToken, String newRefreshToken) {
        String oldKey = generateRedisKey(userId, oldRefreshToken);
        validateRefreshTokenExists(oldKey);
        revoke(userId, oldRefreshToken);

        save(userId, newRefreshToken);
    }

    private String generateRedisKey(Long userId, String token) {
        String hashedToken = HashingUtil.generateHash(token);
        return REDIS_KEYSPACE_REFRESH_TOKEN.formatted(userId, hashedToken);
    }

    private void setRefreshTokenWithExpiration(String key) {
        stringRedisTemplate.opsForValue().set(key, "", Duration.ofMillis(tokenProvider.refreshTokenExpirationMillis));
    }

    private void validateRefreshTokenFormat(String refreshToken) {
        if (!StringUtils.hasText(refreshToken) || !tokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    private void validateRefreshTokenExists(String key) {
        Boolean hasKey = stringRedisTemplate.hasKey(key);
        if (hasKey == null || !hasKey) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}
