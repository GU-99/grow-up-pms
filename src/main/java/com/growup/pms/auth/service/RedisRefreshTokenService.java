package com.growup.pms.auth.service;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import com.growup.pms.common.util.HashingUtil;
import com.growup.pms.user.repository.UserRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenService implements RefreshTokenService {
    private static final String KEYSPACE_REFRESH_TOKEN = "user:%s:refresh-token:%s";

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final StringRedisTemplate stringRedisTemplate;

    public void save(Long userId, String token) {
        String key = generateKey(userId, token);
        setRefreshTokenWithExpiration(key);
    }

    public TokenResponse refreshJwtTokens(String oldRefreshToken) {
        validateRefreshTokenFormat(oldRefreshToken);

        SecurityUser currentUser = (SecurityUser) tokenProvider.getAuthentication(oldRefreshToken).getPrincipal();
        TokenResponse newToken = tokenProvider.generateToken(currentUser);

        renewRefreshToken(currentUser.getId(), oldRefreshToken, newToken.refreshToken());
        return newToken;
    }

    public void revokeRefreshToken(Long userId, String refreshToken) {
        stringRedisTemplate.delete(generateKey(userId, refreshToken));
    }

    private String generateKey(Long userId, String token) {
        String hashedToken = HashingUtil.generateHash(token);
        return KEYSPACE_REFRESH_TOKEN.formatted(userId, hashedToken);
    }

    private void renewRefreshToken(Long userId, String oldRefreshToken, String newRefreshToken) {
        String oldKey = generateKey(userId, oldRefreshToken);
        validateRefreshTokenExists(oldKey);
        revokeRefreshToken(userId, oldRefreshToken);

        String newKey = generateKey(userId, newRefreshToken);
        setRefreshTokenWithExpiration(newKey);
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

    private void setRefreshTokenWithExpiration(String key) {
        stringRedisTemplate.opsForValue().set(key, "", Duration.ofMillis(tokenProvider.refreshTokenExpirationMillis));
    }
}
