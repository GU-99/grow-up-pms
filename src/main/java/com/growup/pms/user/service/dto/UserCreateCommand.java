package com.growup.pms.user.service.dto;

import com.growup.pms.user.domain.Provider;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.domain.UserProfile;
import java.util.List;
import lombok.Builder;

@Builder
public record UserCreateCommand(
        String username,
        String password,
        String email,
        String nickname,
        String bio,
        String imageUrl,
        List<String> links
) {
    public User toEntity() {
        User user = User.builder()
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
        user.addLinks(links);
        return user;
    }
}
