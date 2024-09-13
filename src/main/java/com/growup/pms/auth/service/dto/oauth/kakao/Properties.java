package com.growup.pms.auth.service.dto.oauth.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class Properties {
    private String nickname;
    private String profileImage;
    private String thumbnailImage;

    @Builder
    public Properties(String nickname, String profileImage, String thumbnailImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.thumbnailImage = thumbnailImage;
    }
}
