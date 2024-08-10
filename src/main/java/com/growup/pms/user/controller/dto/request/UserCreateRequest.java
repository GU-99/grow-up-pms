package com.growup.pms.user.controller.dto.request;

import com.growup.pms.user.service.dto.UserCreateCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record UserCreateRequest(
        @Email
        @Length(max = 255)
        String email,

        @Length(min = 8, max = 16)
        String password,

        @NotBlank
        @Length(max = 20)
        String nickname,

        String bio,

        String imageUrl
) {
    public UserCreateCommand toCommand() {
        return UserCreateCommand.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .bio(bio)
                .imageUrl(imageUrl)
                .build();
    }
}
