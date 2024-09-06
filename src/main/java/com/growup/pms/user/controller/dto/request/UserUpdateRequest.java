package com.growup.pms.user.controller.dto.request;

import static com.growup.pms.common.constant.RegexConstants.NICKNAME_PATTERN;

import com.growup.pms.user.service.dto.UserUpdateCommand;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Pattern(regexp = NICKNAME_PATTERN)
    JsonNullable<String> nickname = JsonNullable.undefined();

    @Size(max = 200)
    JsonNullable<String> bio = JsonNullable.undefined();

    @Size(max = 255)
    JsonNullable<String> profileImageUrl = JsonNullable.undefined();

    @Builder
    public UserUpdateRequest(JsonNullable<String> nickname, JsonNullable<String> bio, JsonNullable<String> profileImageUrl) {
        this.nickname = nickname;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
    }

    public UserUpdateCommand toCommand() {
        return UserUpdateCommand.builder()
                .nickname(nickname)
                .bio(bio)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
