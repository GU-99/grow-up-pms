package com.growup.pms.test.fixture.auth.builder;

import com.growup.pms.auth.controller.dto.request.OauthLoginRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OauthLoginTestBuilder {
    private String code = "test_code";

    public static OauthLoginTestBuilder 소셜로그인_하는_사용자의_인가코드는() {
        return new OauthLoginTestBuilder();
    }

    public OauthLoginTestBuilder 인가_코드가(String 인가코드) {
        this.code = 인가코드;
        return this;
    }

    public OauthLoginRequest 이다() {
        return OauthLoginRequest.builder()
                .code(code)
                .build();
    }
}
