package com.growup.pms.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {
    @Column(nullable = false)
    private String nickname;

    private String content;

    private String profileImage;

    @Builder
    public UserProfile(String nickname, String content, String profileImage) {
        this.nickname = nickname;
        this.content = content;
        this.profileImage = profileImage;
    }
}
