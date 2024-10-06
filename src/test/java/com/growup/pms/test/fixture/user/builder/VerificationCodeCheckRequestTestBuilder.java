package com.growup.pms.test.fixture.user.builder;

import com.growup.pms.user.controller.dto.request.VerificationCodeCheckRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VerificationCodeCheckRequestTestBuilder {
    private String email;

    private String verificationCode;

    public static VerificationCodeCheckRequestTestBuilder 인증_코드_확인은() {
        return new VerificationCodeCheckRequestTestBuilder();
    }

    public VerificationCodeCheckRequestTestBuilder 이메일은(String 이메일) {
        this.email = 이메일;
        return this;
    }

    public VerificationCodeCheckRequestTestBuilder 인증코드는(String 인증코드) {
        this.verificationCode = 인증코드;
        return this;
    }

    public VerificationCodeCheckRequest 이다() {
        return VerificationCodeCheckRequest.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();
    }
}
