package com.growup.pms.user.controller.dto.request;

import static com.growup.pms.common.constant.RegexConstants.NICKNAME_PATTERN;

import com.growup.pms.user.service.dto.UserUpdateCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Builder
public record UserUpdateRequest(
        @Pattern(regexp = NICKNAME_PATTERN)
        String nickname,

        @Length(max = 300)
        String bio,

        @Length(max = 255)
        String imageUrl,

        @NotNull
        List<@URL @Length(max = 255) String> links
) {
    public UserUpdateCommand toCommand() {
        return UserUpdateCommand.builder()
                .nickname(nickname)
                .bio(bio)
                .imageUrl(imageUrl)
                .links(links)
                .build();
    }
}
