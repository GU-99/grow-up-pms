package com.growup.pms.auth.service.dto.oauth.kakao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Profile {
    private String nickname;
    private String profile_image_url;
    private String thumbnail_image_url;
    private Boolean is_default_image;
    private Boolean is_default_nickname;

    @Builder
    public Profile(String nickname, String profile_image_url, String thumbnail_image_url, Boolean is_default_image,
                   Boolean is_default_nickname) {
        this.nickname = nickname;
        this.profile_image_url = profile_image_url;
        this.thumbnail_image_url = thumbnail_image_url;
        this.is_default_image = is_default_image;
        this.is_default_nickname = is_default_nickname;
    }
}
