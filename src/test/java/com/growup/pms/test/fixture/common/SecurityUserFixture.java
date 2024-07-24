package com.growup.pms.test.fixture.common;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.test.fixture.user.UserFixture;

public class SecurityUserFixture {

    public static SecurityUser createUserWithUserId(Long userId) {
        return createDefaultUserBuilder().id(userId)
                .build();
    }

    public static SecurityUser.SecurityUserBuilder createDefaultUserBuilder() {
        return SecurityUser.builder()
                .id(UserFixture.DEFAULT_ID)
                .email(UserFixture.DEFAULT_EMAIL)
                .password(UserFixture.DEFAULT_PASSWORD);
    }
}
