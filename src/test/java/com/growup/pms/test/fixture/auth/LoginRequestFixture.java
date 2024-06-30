package com.growup.pms.test.fixture.auth;

import static com.growup.pms.test.fixture.user.UserFixture.DEFAULT_PASSWORD;
import static com.growup.pms.test.fixture.user.UserFixture.DEFAULT_EMAIL;

import com.growup.pms.auth.dto.LoginRequest;

public class LoginRequestFixture {

    public static LoginRequest createDefaultRequest() {
        return createDefaultRequestBuilder().build();
    }

    public static LoginRequest.LoginRequestBuilder createDefaultRequestBuilder() {
        return LoginRequest.builder()
                .email(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD);
    }
}
