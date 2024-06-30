package com.growup.pms.test.fixture.user;

import static com.growup.pms.test.fixture.user.UserFixture.*;

import com.growup.pms.user.domain.User;
import com.growup.pms.user.dto.UserCreateRequest;

public class UserCreateRequestFixture {

    public static UserCreateRequest createDefaultRequest() {
        return createDefaultRequestBuilder().build();
    }

    public static UserCreateRequest createRequestFromUser(User user) {
        return UserCreateRequest.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .passwordConfirm(user.getPassword())
                .nickname(user.getProfile().getNickname())
                .bio(user.getProfile().getBio())
                .image(user.getProfile().getImage())
                .build();
    }

    public static UserCreateRequest.UserCreateRequestBuilder createDefaultRequestBuilder() {
        return UserCreateRequest.builder()
                .email(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
                .passwordConfirm(DEFAULT_PASSWORD)
                .nickname(DEFAULT_NICKNAME)
                .bio(DEFAULT_BIO)
                .image(DEFAULT_IMAGE);
    }
}
