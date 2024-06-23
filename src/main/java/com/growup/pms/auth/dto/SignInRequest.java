package com.growup.pms.auth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignInRequest {
    private String username;

    private String password;

    @Builder
    public SignInRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
