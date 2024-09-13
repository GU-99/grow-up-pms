package com.growup.pms.auth.service.dto.oauth.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class Profile {
    private String nickname;
    private String profileImageUrl;
    private String thumbnailImageUrl;
    private Boolean isDefaultImage;
    private Boolean isDefaultNickname;

    @Builder
    public Profile(String nickname, String profileImageUrl, String thumbnailImageUrl, Boolean isDefaultImage,
                   Boolean isDefaultNickname) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.isDefaultImage = isDefaultImage;
        this.isDefaultNickname = isDefaultNickname;
    }
}
