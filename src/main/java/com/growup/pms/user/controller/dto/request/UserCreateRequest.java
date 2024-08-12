package com.growup.pms.user.controller.dto.request;

import com.growup.pms.user.service.dto.UserCreateCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record UserCreateRequest(
        @Pattern(regexp = "^[a-zA-Z0-9]{2,32}$")
        String username,

        @Length(min = 8, max = 16)
        String password,

        @Email
        @Length(max = 128)
        String email,

        @Length(min = 2, max = 20)
        String nickname,

        @Length(max = 300)
        String bio,

        @Length(max = 255)
        String imageUrl
) {
    public UserCreateCommand toCommand() {
        return UserCreateCommand.builder()
                .username(username)
                .password(password)
                .email(email)
                .nickname(nickname)
                .bio(bio)
                .imageUrl(imageUrl)
                .build();
    }
}
