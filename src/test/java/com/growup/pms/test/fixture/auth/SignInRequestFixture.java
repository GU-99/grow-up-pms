package com.growup.pms.test.fixture.auth;

import static com.growup.pms.test.fixture.user.UserFixture.DEFAULT_PASSWORD;
import static com.growup.pms.test.fixture.user.UserFixture.DEFAULT_USERNAME;

import com.growup.pms.auth.dto.SignInRequest;

public class SignInRequestFixture {

    public static SignInRequest createDefaultRequest() {
        return createDefaultRequestBuilder().build();
    }

    public static SignInRequest.SignInRequestBuilder createDefaultRequestBuilder() {
        return SignInRequest.builder()
                .username(DEFAULT_USERNAME)
                .password(DEFAULT_PASSWORD);
    }
}
