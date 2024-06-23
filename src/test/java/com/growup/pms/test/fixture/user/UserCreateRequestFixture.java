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
                .username(user.getUsername())
                .password(user.getPassword())
                .passwordConfirm(user.getPassword())
                .nickname(user.getProfile().getNickname())
                .content(user.getProfile().getContent())
                .profileImage(user.getProfile().getProfileImage())
                .build();
    }

    public static UserCreateRequest.UserCreateRequestBuilder createDefaultRequestBuilder() {
        return UserCreateRequest.builder()
                .username(DEFAULT_USERNAME)
                .password(DEFAULT_PASSWORD)
                .passwordConfirm(DEFAULT_PASSWORD)
                .nickname(DEFAULT_NICKNAME)
                .content(DEFAULT_CONTENT)
                .profileImage(DEFAULT_PROFILE_IMAGE);
    }
}
