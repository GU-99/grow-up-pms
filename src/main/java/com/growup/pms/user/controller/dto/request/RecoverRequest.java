package com.growup.pms.user.controller.dto.request;

import com.growup.pms.user.service.dto.RecoverCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
// TODO: 좀 더 의미있는 이름으로 변경해보자
public record RecoverRequest(
        @Email
        String email,

        @Size(min = 6, max = 6)
        String verificationCode
) {
    public RecoverCommand toCommand() {
        return RecoverCommand.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();
    }
}
