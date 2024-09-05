package com.growup.pms.test.fixture.auth.builder;

import com.growup.pms.auth.controller.dto.request.LoginRequest;
import com.growup.pms.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequestTestBuilder {
    private String username = "brown";
    private String password = "test1234!@#$";

    public static LoginRequestTestBuilder 로그인_하는_사용자는() {
        return new LoginRequestTestBuilder();
    }

    public static LoginRequestTestBuilder 로그인_하는_사용자는(User 사용자) {
        return new LoginRequestTestBuilder()
                .아이디가(사용자.getUsername())
                .비밀번호가(사용자.getPassword());
    }

    public LoginRequestTestBuilder 아이디가(String 아이디) {
        this.username = 아이디;
        return this;
    }

    public LoginRequestTestBuilder 비밀번호가(String 비밀번호) {
        this.password = 비밀번호;
        return this;
    }

    public LoginRequest 이다() {
        return LoginRequest.builder()
                .username(username)
                .password(password)
                .build();
    }
}
