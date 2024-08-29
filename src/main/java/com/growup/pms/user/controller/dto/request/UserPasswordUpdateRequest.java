package com.growup.pms.user.controller.dto.request;

import static com.growup.pms.common.constant.RegexConstants.PASSWORD_PATTERN;

import com.growup.pms.user.service.dto.UserPasswordUpdateCommand;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UserPasswordUpdateRequest(
        @Pattern(regexp = PASSWORD_PATTERN)
        String password,

        @Pattern(regexp = PASSWORD_PATTERN)
        String passwordNew
) {

    public UserPasswordUpdateCommand toCommand() {
        return UserPasswordUpdateCommand.builder()
                .password(password)
                .passwordNew(passwordNew)
                .build();
    }
}
