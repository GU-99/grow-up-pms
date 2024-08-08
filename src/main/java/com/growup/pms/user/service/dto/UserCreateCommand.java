package com.growup.pms.user.service.dto;

import com.growup.pms.user.domain.Provider;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.domain.UserProfile;
import lombok.Builder;

@Builder
public record UserCreateCommand(
        String email,
        String password,
        String nickname,
        String bio,
        String image
) {
    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .provider(Provider.LOCAL)
                .profile(UserProfile.builder()
                        .nickname(nickname)
                        .bio(bio)
                        .image(image)
                        .build())
                .build();
    }
}
