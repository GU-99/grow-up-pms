package com.growup.pms.auth.controller.dto.request;

import static com.growup.pms.common.constant.RegexConstants.PASSWORD_PATTERN;
import static com.growup.pms.common.constant.RegexConstants.USERNAME_PATTERN;

import com.growup.pms.auth.service.dto.LoginCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record LoginRequest(
        @NotNull
        @Pattern(regexp = USERNAME_PATTERN)
        String username,

        @NotNull
        @Pattern(regexp = PASSWORD_PATTERN)
        String password
) {
    public LoginCommand toCommand() {
        return LoginCommand.builder()
                .username(username)
                .password(password)
                .build();
    }
}
