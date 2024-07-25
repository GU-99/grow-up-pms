package com.growup.pms.test.fixture.user;

import com.growup.pms.user.domain.User;
import com.growup.pms.user.dto.UserCreateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCreateRequestTestBuilder {
    private String nickname = "브라운";
    private String email = "brown@gu99.com";
    private String password = "test1234!@#$";
    private String passwordConfirm = "test1234!@#$";
    private String bio = "안녕하세요, 브라운입니다!";

    public static UserCreateRequestTestBuilder 가입하는_사용자는() {
        return new UserCreateRequestTestBuilder();
    }

    public static UserCreateRequestTestBuilder 가입하는_사용자는(User 사용자) {
        var builder = new UserCreateRequestTestBuilder();
        builder.nickname = 사용자.getProfile().getNickname();
        builder.email = 사용자.getEmail();
        builder.password = 사용자.getPassword();
        builder.passwordConfirm = 사용자.getPassword();
        builder.bio = 사용자.getProfile().getBio();
        return builder;
    }

    public UserCreateRequestTestBuilder 닉네임이(String 닉네임) {
        this.nickname = 닉네임;
        return this;
    }

    public UserCreateRequestTestBuilder 이메일이(String 이메일) {
        this.email = 이메일;
        return this;
    }

    public UserCreateRequestTestBuilder 비밀번호가(String 비밀번호) {
        this.password = 비밀번호;
        return this;
    }

    public UserCreateRequestTestBuilder 비밀번호_확인이(String 비밀번호_확인) {
        this.passwordConfirm = 비밀번호_확인;
        return this;
    }

    public UserCreateRequestTestBuilder 자기소개가(String 자기소개) {
        this.bio = 자기소개;
        return this;
    }

    public UserCreateRequest 이다() {
        return UserCreateRequest.builder()
                .email(email)
                .password(password)
                .passwordConfirm(passwordConfirm)
                .bio(bio)
                .nickname(nickname)
                .build();
    }
}
