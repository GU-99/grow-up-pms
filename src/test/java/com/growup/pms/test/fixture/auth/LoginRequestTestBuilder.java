package com.growup.pms.test.fixture.auth;

import com.growup.pms.auth.controller.dto.request.LoginRequest;
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
