package com.growup.pms.auth.repository;

import com.growup.pms.auth.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Long deleteByUserId(Long userId);

    Optional<RefreshToken> findByUserId(Long userId);

    Optional<RefreshToken> findByUserIdAndToken(Long userId, String token);
}
