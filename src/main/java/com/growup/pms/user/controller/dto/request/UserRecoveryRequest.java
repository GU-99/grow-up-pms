package com.growup.pms.user.controller.dto.request;

import com.growup.pms.user.service.dto.UserRecoveryCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRecoveryRequest(
        @Email
        String email,

        @Size(min = 6, max = 6)
        String verificationCode
) {
    public UserRecoveryCommand toCommand() {
        return UserRecoveryCommand.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();
    }
}
