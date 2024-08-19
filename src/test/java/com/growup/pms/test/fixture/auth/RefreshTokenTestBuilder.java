package com.growup.pms.test.fixture.auth;

import com.growup.pms.auth.domain.RefreshToken;
import com.growup.pms.test.fixture.user.UserTestBuilder;
import com.growup.pms.user.domain.User;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshTokenTestBuilder {
    private Long id = 1L;
    private User user = UserTestBuilder.사용자는().이다();
    private String token = "유효한 리프레시 토큰";
    private LocalDateTime expiredAt = LocalDateTime.now().plus(1000, ChronoUnit.MILLIS);

    public static RefreshTokenTestBuilder 리프레시_토큰은() {
        return new RefreshTokenTestBuilder();
    }

    public RefreshTokenTestBuilder 사용자가(User 사용자) {
        this.user = 사용자;
        return this;
    }

    public RefreshTokenTestBuilder 리프레시_토큰이(String 리프레시_토큰) {
        this.token = 리프레시_토큰;
        return this;
    }

    public RefreshTokenTestBuilder 만료기한이(LocalDateTime 만료기한) {
        this.expiredAt = 만료기한;
        return this;
    }

    public RefreshToken 이다() {
        var refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiryDate(expiredAt)
                .build();
        ReflectionTestUtils.setField(refreshToken, "id", id);
        return refreshToken;
    }
}
