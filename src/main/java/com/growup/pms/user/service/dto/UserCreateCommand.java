package com.growup.pms.user.service.dto;

import com.growup.pms.user.domain.Provider;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.domain.UserProfile;
import lombok.Builder;

@Builder
public record UserCreateCommand(
        String username,
        String password,
        String email,
        String nickname,
        String bio,
        String imageUrl
) {
    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .provider(Provider.LOCAL)
                .profile(UserProfile.builder()
                        .nickname(nickname)
                        .bio(bio)
                        .image(imageUrl)
                        .build())
                .build();
    }
}
