package com.growup.pms.auth.service;

import com.growup.pms.auth.domain.RefreshToken;
import com.growup.pms.auth.repository.RefreshTokenRepository;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.util.HashingUtil;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Transactional
    public Long save(Long userId, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByIdOrThrow(userId))
                .token(HashingUtil.generateHash(token))
                .expiryDate(LocalDateTime.now().plus(tokenProvider.refreshTokenExpirationMillis, ChronoUnit.MILLIS))
                .build();
        return refreshTokenRepository.save(refreshToken).getId();
    }

    @Transactional
    public Long renewRefreshToken(Long userId, String newRefreshToken) {
        return refreshTokenRepository.findByUserId(userId)
                .map(token -> {
                    token.renewToken(newRefreshToken, LocalDateTime.now().plus(tokenProvider.refreshTokenExpirationMillis, ChronoUnit.MILLIS));
                    return token.getId();
                })
                .orElseGet(() -> save(userId, newRefreshToken));
    }

    public boolean validateToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            return false;
        }

        Optional<RefreshToken> storedRefreshToken = getStoredRefreshToken(token);
        return storedRefreshToken.isPresent() && !isTokenExpired(storedRefreshToken.get());
    }

    private Optional<RefreshToken> getStoredRefreshToken(String token) {
        User user = userRepository.findByIdOrThrow(tokenProvider.getUserIdFromToken(token));
        return refreshTokenRepository.findByUserIdAndToken(user.getId(), HashingUtil.generateHash(token));
    }

    private boolean isTokenExpired(RefreshToken token) {
        return token.getExpiredAt().isBefore(LocalDateTime.now());
    }
}
