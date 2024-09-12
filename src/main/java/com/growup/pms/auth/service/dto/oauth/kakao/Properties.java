package com.growup.pms.auth.service.dto.oauth.kakao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Properties {
    private String nickname;
    private String profile_image;
    private String thumbnail_image;

    @Builder
    public Properties(String nickname, String profile_image, String thumbnail_image) {
        this.nickname = nickname;
        this.profile_image = profile_image;
        this.thumbnail_image = thumbnail_image;
    }
}
