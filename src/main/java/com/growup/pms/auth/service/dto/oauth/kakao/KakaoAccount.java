package com.growup.pms.auth.service.dto.oauth.kakao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoAccount {
    private String email;
    private Boolean is_email_verified;
    private Boolean has_email;
    private Boolean profile_nickname_needs_agreement;
    private Boolean profile_image_needs_agreement;
    private Boolean email_needs_agreement;
    private Boolean is_email_valid;
    private Profile profile;

    @Builder
    public KakaoAccount(String email, Boolean is_email_verified, Boolean has_email,
                        Boolean profile_nickname_needs_agreement, Boolean profile_image_needs_agreement,
                        Boolean email_needs_agreement, Boolean is_email_valid, Profile profile) {
        this.email = email;
        this.is_email_verified = is_email_verified;
        this.has_email = has_email;
        this.profile_nickname_needs_agreement = profile_nickname_needs_agreement;
        this.profile_image_needs_agreement = profile_image_needs_agreement;
        this.email_needs_agreement = email_needs_agreement;
        this.is_email_valid = is_email_valid;
        this.profile = profile;
    }
}
