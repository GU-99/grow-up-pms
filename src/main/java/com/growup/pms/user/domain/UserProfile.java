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

    private String bio;

    private String image;

    private String imageName;

    @Builder
    public UserProfile(String nickname, String bio, String image, String imageName) {
        this.nickname = nickname;
        this.bio = bio;
        this.image = image;
        this.imageName = imageName;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeBio(String bio) {
        this.bio = bio;
    }

    public void changeImage(String image) {
        this.image = image;
    }

    public void changeImageName(String imageName) {
        this.imageName = imageName;
    }
}
