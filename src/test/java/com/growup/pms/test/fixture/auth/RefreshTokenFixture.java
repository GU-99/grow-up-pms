package com.growup.pms.test.fixture.auth;

import com.growup.pms.auth.domain.RefreshToken;
import com.growup.pms.user.domain.User;
import java.time.Instant;
import org.springframework.test.util.ReflectionTestUtils;

public class RefreshTokenFixture {
    public static final Long DEFAULT_REFRESH_TOKEN_ID = 1L;

    public static final String VALID_REFRESH_TOKEN = "validRefreshToken";
    public static final String INVALID_REFRESH_TOKEN = "invalidRefreshToken";

    public static final Instant VALID_EXPIRY_DATE = Instant.now().plusMillis(1000);
    public static final Instant INVALID_EXPIRY_DATE = Instant.now().minusMillis(1000);

    public static RefreshToken createRefreshTokenWithUser(User user) {
        RefreshToken refreshToken = createDefaultTokenBuilder().user(user)
                .build();
        ReflectionTestUtils.setField(refreshToken, "id", DEFAULT_REFRESH_TOKEN_ID);
        return refreshToken;
    }

    public static RefreshToken createRefreshTokenWithUserAndExpiryDate(User user, Instant expiryDate) {
        RefreshToken refreshToken = createDefaultTokenBuilder().user(user)
                .expiryDate(expiryDate)
                .build();
        ReflectionTestUtils.setField(refreshToken, "id", DEFAULT_REFRESH_TOKEN_ID);
        return refreshToken;
    }

    public static RefreshToken.RefreshTokenBuilder createDefaultTokenBuilder() {
        return RefreshToken.builder()
                .token(VALID_REFRESH_TOKEN)
                .expiryDate(VALID_EXPIRY_DATE);
    }
}
