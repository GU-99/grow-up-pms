package com.growup.pms.test.fixture.user;

import com.growup.pms.user.controller.dto.request.UserPasswordUpdateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserPasswordUpdateTestBuilder {

    private String password = "test1234!@#$";
    private String passwordNew = "test2345!@#$";

    public static UserPasswordUpdateTestBuilder 비밀번호_변경은() {
        return new UserPasswordUpdateTestBuilder();
    }

    public UserPasswordUpdateTestBuilder 기존_비밀번호가(String 기존_비밀번호) {
        this.password = 기존_비밀번호;
        return this;
    }

    public UserPasswordUpdateTestBuilder 새로운_비밀번호가(String 새로운_비밀번호) {
        this.passwordNew = 새로운_비밀번호;
        return this;
    }

    public UserPasswordUpdateRequest 이다() {
        return UserPasswordUpdateRequest.builder()
                .password(password)
                .passwordNew(passwordNew)
                .build();
    }

}
