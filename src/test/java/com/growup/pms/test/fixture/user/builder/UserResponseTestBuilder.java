package com.growup.pms.test.fixture.user.builder;

import com.growup.pms.user.controller.dto.response.UserResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseTestBuilder {
    private String username = "brown";
    private String email = "brown@growup.kr";
    private String nickname = "브라운";
    private String bio = "안녕하세요, 브라운입니다!";
    private String profileImageUrl = "https://growup.kr/images/image.png";
    private List<String> links = List.of("https://github.com/growup");

    public static UserResponseTestBuilder 사용자_조회_응답은() {
        return new UserResponseTestBuilder();
    }

    public UserResponseTestBuilder 아이디가(String 아이디) {
        this.username = 아이디;
        return this;
    }

    public UserResponseTestBuilder 이메일이(String 이메일) {
        this.email = 이메일;
        return this;
    }

    public UserResponseTestBuilder 닉네임이(String 닉네임) {
        this.nickname = 닉네임;
        return this;
    }

    public UserResponseTestBuilder 자기소개가(String 자기소개) {
        this.bio = 자기소개;
        return this;
    }

    public UserResponseTestBuilder 프로필_이미지가(String 프로필_이미지) {
        this.profileImageUrl = 프로필_이미지;
        return this;
    }

    public UserResponseTestBuilder 링크가(List<String> 링크) {
        this.links = 링크;
        return this;
    }

    public UserResponse 이다() {
        return UserResponse.builder()
                .username(username)
                .email(email)
                .nickname(nickname)
                .bio(bio)
                .profileImageUrl(profileImageUrl)
                .links(links)
                .build();
    }
}
