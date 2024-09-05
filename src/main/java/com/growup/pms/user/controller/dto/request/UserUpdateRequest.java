package com.growup.pms.user.controller.dto.request;

import static com.growup.pms.common.constant.RegexConstants.NICKNAME_PATTERN;

import com.growup.pms.user.service.dto.UserUpdateCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Builder
public record UserUpdateRequest(
        @NotNull
        @Pattern(regexp = NICKNAME_PATTERN)
        String nickname,

        @NotNull
        @Length(max = 200)
        String bio,

        @Length(max = 255)
        String profileImageUrl,

        @NotNull
        List<@NotNull @URL @Size(max = 255) String> links
) {
    public UserUpdateCommand toCommand() {
        return UserUpdateCommand.builder()
                .nickname(nickname)
                .bio(bio)
                .profileImageUrl(profileImageUrl)
                .links(links)
                .build();
    }
}
