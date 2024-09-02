package com.growup.pms.user.controller.dto.request;

import com.growup.pms.user.service.dto.VerificationCodeCreateCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record VerificationCodeCreateRequest(
        @NotNull
        @Email
        String email
) {
    public VerificationCodeCreateCommand toCommand() {
        return new VerificationCodeCreateCommand(email);
    }
}
