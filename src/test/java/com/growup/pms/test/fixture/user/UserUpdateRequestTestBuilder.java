package com.growup.pms.test.fixture.user;

import com.growup.pms.user.controller.dto.request.UserUpdateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUpdateRequestTestBuilder {
    private String nickname = "growUp";
    private String bio;
    private String profileImageUrl;

    public static UserUpdateRequestTestBuilder 사용자_정보_변경_요청은() {
        return new UserUpdateRequestTestBuilder();
    }

    public UserUpdateRequestTestBuilder 닉네임이(String 닉네임) {
        this.nickname = 닉네임;
        return this;
    }

    public UserUpdateRequestTestBuilder 자기소개는(String 자기소개) {
        this.bio = 자기소개;
        return this;
    }

    public UserUpdateRequestTestBuilder 프로필_이미지_URL이(String 프로필_이미지_URL) {
        this.profileImageUrl = 프로필_이미지_URL;
        return this;
    }

    public UserUpdateRequest 이다() {
        return UserUpdateRequest.builder()
                .nickname(JsonNullable.of(nickname))
                .bio(JsonNullable.of(bio))
                .profileImageUrl(JsonNullable.of(profileImageUrl))
                .build();
    }
}
