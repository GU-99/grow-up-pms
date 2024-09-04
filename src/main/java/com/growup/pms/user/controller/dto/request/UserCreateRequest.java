package com.growup.pms.user.controller.dto.request;

import static com.growup.pms.common.constant.RegexConstants.NICKNAME_PATTERN;
import static com.growup.pms.common.constant.RegexConstants.PASSWORD_PATTERN;
import static com.growup.pms.common.constant.RegexConstants.USERNAME_PATTERN;

import com.growup.pms.user.service.dto.UserCreateCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Builder
public record UserCreateRequest(
        @Pattern(regexp = USERNAME_PATTERN)
        String username,

        @Pattern(regexp = PASSWORD_PATTERN)
        String password,

        @Email
        @Length(max = 128)
        String email,

        @Pattern(regexp = NICKNAME_PATTERN)
        String nickname,

        @Length(max = 300)
        String bio,

        @Length(max = 255)
        String profileImageUrl,

        @NotNull
        List<@URL @Length(max = 255) String> links,

        @NotNull
        String verificationCode
) {
    public UserCreateCommand toCommand() {
        return UserCreateCommand.builder()
                .username(username)
                .password(password)
                .email(email)
                .nickname(nickname)
                .bio(bio)
                .profileImageUrl(profileImageUrl)
                .links(links)
                .verificationCode(verificationCode)
                .build();
    }
}
