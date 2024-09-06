package com.growup.pms.user.service.dto;

import lombok.Builder;
import org.openapitools.jackson.nullable.JsonNullable;

@Builder
public record UserUpdateCommand(
        JsonNullable<String> nickname,
        JsonNullable<String> bio,
        JsonNullable<String> profileImageUrl
) {
}
