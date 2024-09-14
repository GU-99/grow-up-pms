package com.growup.pms.test.fixture.user.builder;

import com.growup.pms.user.controller.dto.response.UserResponse;
import com.growup.pms.user.domain.Provider;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseTestBuilder {
    private Long userId = 1L;
    private String username = "brown";
    private String email = "brown@growup.kr";
    private Provider provider = Provider.LOCAL;
    private String nickname = "브라운";
    private String bio = "안녕하세요, 브라운입니다!";
    private String profileImageName = "728f3af8-4080-45b1-8b3c-d25e5e073dc7.png";
    private List<String> links = List.of("https://github.com/growup");

    public static UserResponseTestBuilder 사용자_조회_응답은() {
        return new UserResponseTestBuilder();
    }

    public UserResponseTestBuilder 식별자가(Long 식별자) {
        this.userId = 식별자;
        return this;
    }

    public UserResponseTestBuilder 아이디가(String 아이디) {
        this.username = 아이디;
        return this;
    }

    public UserResponseTestBuilder 이메일이(String 이메일) {
        this.email = 이메일;
        return this;
    }

    public UserResponseTestBuilder 프로바이더가(Provider 프로바이더) {
        this.provider = 프로바이더;
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

    public UserResponseTestBuilder 프로필_이미지_이름이(String 프로필_이미지_이름) {
        this.profileImageName = 프로필_이미지_이름;
        return this;
    }

    public UserResponseTestBuilder 링크가(List<String> 링크) {
        this.links = 링크;
        return this;
    }

    public UserResponse 이다() {
        return UserResponse.builder()
                .userId(userId)
                .username(username)
                .email(email)
                .provider(provider)
                .nickname(nickname)
                .bio(bio)
                .profileImageName(profileImageName)
                .links(links)
                .build();
    }
}
