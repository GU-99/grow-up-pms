package com.growup.pms.test.fixture.user;

import com.growup.pms.user.controller.dto.request.RecoverUsernameRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecoverUsernameRequestTestBuilder {
    private String email = "brown@example.com";
    private String verificationCode = "123456";

    public static RecoverUsernameRequestTestBuilder 아이디_찾기_요청은() {
        return new RecoverUsernameRequestTestBuilder();
    }

    public RecoverUsernameRequestTestBuilder 이메일이(String 이메일) {
        this.email = 이메일;
        return this;
    }

    public RecoverUsernameRequestTestBuilder 인증번호가(String 인증번호) {
        this.verificationCode = 인증번호;
        return this;
    }

    public RecoverUsernameRequest 이다() {
        return RecoverUsernameRequest.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();
    }
}
