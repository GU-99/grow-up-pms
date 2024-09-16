package com.growup.pms.auth.service.dto.oauth.google;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleProfile {
    private String id;
    private String email;
    private Boolean verified_email;
    private String picture;
    private String hd;

    @Builder
    public GoogleProfile(String id, String email, Boolean verified_email, String picture, String hd) {
        this.id = id;
        this.email = email;
        this.verified_email = verified_email;
        this.picture = picture;
        this.hd = hd;
    }
}
