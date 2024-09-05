package com.growup.pms.test.fixture.user;

import com.growup.pms.user.controller.dto.request.UserUpdateRequest;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUpdateRequestTestBuilder {
    private String nickname = "growUp";
    private String bio = "안녕하세요 저는 grow up입니다. 잘부탁드려요~";
    private String profileImageUrl = "http://example.com/profile.png";
    private List<String> links = List.of("http://github.com", "http://blog.example.com");

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

    public UserUpdateRequestTestBuilder 링크가(List<String> 링크) {
        this.links = 링크;
        return this;
    }

    public UserUpdateRequest 이다() {
        return UserUpdateRequest.builder()
                .nickname(nickname)
                .bio(bio)
                .profileImageUrl(profileImageUrl)
                .links(links)
                .build();
    }
}
