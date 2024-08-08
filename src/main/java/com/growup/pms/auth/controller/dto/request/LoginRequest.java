package com.growup.pms.auth.controller.dto.request;

import com.growup.pms.auth.service.dto.LoginCommand;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record LoginRequest(
        @Length(max = 255, message = "이메일이 너무 깁니다.")
        String email,

        @Length(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하로 입력해주세요.")
        String password
) {
    public LoginCommand toCommand() {
        return LoginCommand.builder()
                .email(email)
                .password(password)
                .build();
    }
}
