package com.growup.pms.user.controller.dto.request;

import com.growup.pms.common.validation.constraint.FieldMatch;
import com.growup.pms.user.service.dto.UserCreateCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
@FieldMatch(first = "password", second = "passwordConfirm")
public record UserCreateRequest(
        @Email
        @Length(max = 255, message = "이메일이 너무 깁니다.")
        String email,

        @Length(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하로 입력해주세요.")
        String password,

        @Length(min = 8, max = 16, message = "비밀번호 확인은 8자 이상 16자 이하로 입력해주세요.")
        String passwordConfirm,

        @NotBlank
        @Length(max = 20)
        String nickname,

        String bio,

        String image
) {
    public UserCreateCommand toCommand() {
        return UserCreateCommand.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .bio(bio)
                .image(image)
                .build();
    }
}
