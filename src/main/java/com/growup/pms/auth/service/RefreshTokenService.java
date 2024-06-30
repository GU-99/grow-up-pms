package com.growup.pms.auth.service;

import com.growup.pms.auth.domain.RefreshToken;
import com.growup.pms.auth.repository.RefreshTokenRepository;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.util.HashingUtil;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    public void deleteTokenByUserId(Long userId) {
        refreshTokenRepository.deleteById(userId);
        refreshTokenRepository.flush();
    }

    public Long save(Long userId, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByIdOrThrow(userId))
                .token(HashingUtil.generateHash(token))
                .expiryDate(Instant.now().plusMillis(tokenProvider.refreshTokenExpirationTime))
                .build();
        return refreshTokenRepository.save(refreshToken).getId();
    }

    @Transactional
    public void updateRefreshToken(Long userId, String refreshToken) {
        deleteTokenByUserId(userId);
        save(userId, refreshToken);
    }

    public boolean validateToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            return false;
        }

        Optional<RefreshToken> storedRefreshToken = getStoredRefreshToken(token);
        return storedRefreshToken.isPresent() && !isTokenExpired(storedRefreshToken.get());
    }

    private Optional<RefreshToken> getStoredRefreshToken(String token) {
        String username = tokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByUsernameOrThrow(username);
        return refreshTokenRepository.findByUserIdAndToken(user.getId(), HashingUtil.generateHash(token));
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }
}
