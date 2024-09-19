package com.growup.pms.auth.service.dto.oauth.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

@Builder
@JsonNaming(SnakeCaseStrategy.class)
public record Profile(
        String nickname,
        String profileImageUrl,
        String thumbnailImageUrl,
        Boolean isDefaultImage,
        Boolean isDefaultNickname
) {
}
