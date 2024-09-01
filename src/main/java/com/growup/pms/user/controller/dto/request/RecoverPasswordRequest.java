package com.growup.pms.user.controller.dto.request;

import static com.growup.pms.common.constant.RegexConstants.USERNAME_PATTERN;

import com.growup.pms.user.service.dto.RecoverPasswordCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RecoverPasswordRequest(
        @Email
        String email,

        @Pattern(regexp = USERNAME_PATTERN)
        String username,

        @Size(min = 6, max = 6)
        String verificationCode
) {
    public RecoverPasswordCommand toCommand() {
        return RecoverPasswordCommand.builder()
                .email(email)
                .username(username)
                .verificationCode(verificationCode)
                .build();
    }
}
