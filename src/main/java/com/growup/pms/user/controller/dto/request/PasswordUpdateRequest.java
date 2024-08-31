package com.growup.pms.user.controller.dto.request;

import static com.growup.pms.common.constant.RegexConstants.PASSWORD_PATTERN;

import com.growup.pms.user.service.dto.PasswordUpdateCommand;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record PasswordUpdateRequest(
        @Pattern(regexp = PASSWORD_PATTERN)
        String password,

        @Pattern(regexp = PASSWORD_PATTERN)
        String newPassword
) {

    public PasswordUpdateCommand toCommand() {
        return PasswordUpdateCommand.builder()
                .password(password)
                .newPassword(newPassword)
                .build();
    }
}
