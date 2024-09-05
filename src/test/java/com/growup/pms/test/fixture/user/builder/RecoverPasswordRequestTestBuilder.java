package com.growup.pms.test.fixture.user.builder;

import com.growup.pms.user.controller.dto.request.RecoverPasswordRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecoverPasswordRequestTestBuilder {
    private String email = "brown@example.com";
    private String username = "brown";
    private String verificationCode = "123456";

    public static RecoverPasswordRequestTestBuilder 비밀번호_찾기_요청은() {
        return new RecoverPasswordRequestTestBuilder();
    }

    public RecoverPasswordRequestTestBuilder 이메일이(String 이메일) {
        this.email = 이메일;
        return this;
    }

    public RecoverPasswordRequestTestBuilder 아이디가(String 아이디) {
        this.username = 아이디;
        return this;
    }

    public RecoverPasswordRequestTestBuilder 인증번호가(String 인증번호) {
        this.verificationCode = 인증번호;
        return this;
    }

    public RecoverPasswordRequest 이다() {
        return RecoverPasswordRequest.builder()
                .email(email)
                .username(username)
                .verificationCode(verificationCode)
                .build();
    }
}
