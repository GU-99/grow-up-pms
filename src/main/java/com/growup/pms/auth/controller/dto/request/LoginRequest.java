package com.growup.pms.auth.controller.dto.request;

import com.growup.pms.auth.service.dto.LoginCommand;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record LoginRequest(
        @Pattern(regexp = "^[a-zA-Z0-9]{2,32}$")
        String username,

        @Length(min = 8, max = 16)
        String password
) {
    public LoginCommand toCommand() {
        return LoginCommand.builder()
                .username(username)
                .password(password)
                .build();
    }
}
