package com.growup.pms.test.fixture.auth.builder;

import com.growup.pms.auth.service.dto.oauth.google.GoogleProfile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GoogleProfileTestBuilder {
    private String id = "105089207533";
    private String email = "test@test.com";

    public static GoogleProfileTestBuilder 구글_프로필은() {
        return new GoogleProfileTestBuilder();
    }

    public GoogleProfileTestBuilder 아이디가(String 아이디) {
        this.id = 아이디;
        return this;
    }

    public GoogleProfileTestBuilder 이메일이(String 이메일) {
        this.email = 이메일;
        return this;
    }

    public GoogleProfile 이다() {
        return GoogleProfile.builder()
                .id(id)
                .email(email)
                .build();
    }
}
