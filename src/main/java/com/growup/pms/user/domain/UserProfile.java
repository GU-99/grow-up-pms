package com.growup.pms.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {
    @Column(nullable = false)
    private String nickname;

    private String bio;

    @Setter
    private String image;

    @Builder
    public UserProfile(String nickname, String bio, String image) {
        this.nickname = nickname;
        this.bio = bio;
        this.image = image;
    }
}
