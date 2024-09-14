package com.growup.pms.user.controller.dto.response;

import com.growup.pms.user.domain.Provider;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.domain.UserLink;
import java.util.List;
import lombok.Builder;

@Builder
public record UserResponse(
        Long userId,
        String username,
        String email,
        Provider provider,
        String nickname,
        String bio,
        String profileImageName,
        List<String> links
) {

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .provider(user.getProvider())
                .nickname(user.getProfile().getNickname())
                .bio(user.getProfile().getBio())
                .profileImageName(user.getProfile().getImageName())
                .links(user.getLinks().stream().map(UserLink::getLink).toList())
                .build();
    }
}
