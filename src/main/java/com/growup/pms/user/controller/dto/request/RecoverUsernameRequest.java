package com.growup.pms.user.controller.dto.request;

import com.growup.pms.user.service.dto.RecoverUsernameCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RecoverUsernameRequest(
        @Email
        String email,

        @Size(min = 6, max = 6)
        String verificationCode
) {
    public RecoverUsernameCommand toCommand() {
        return RecoverUsernameCommand.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();
    }
}
