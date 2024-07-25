package com.growup.pms.test.fixture.auth;

import com.growup.pms.auth.domain.SecurityUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUserTestBuilder {
    private Long id = 1L;
    private String email = "brown@gu99.com";
    private String password = "test1234!@#$";

    public static SecurityUserTestBuilder 인증된_사용자는() {
        return new SecurityUserTestBuilder();
    }

    public SecurityUserTestBuilder 식별자가(Long 식별자) {
        this.id = 식별자;
        return this;
    }

    public SecurityUserTestBuilder 이메일이(String 이메일) {
        this.email = 이메일;
        return this;
    }

    public SecurityUserTestBuilder 비밀번호가(String 비밀번호) {
        this.password = 비밀번호;
        return this;
    }

    public SecurityUser 이다() {
        return SecurityUser.builder()
                .id(id)
                .email(email)
                .password(password)
                .build();
    }
}
