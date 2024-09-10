package com.growup.pms.user.controller.dto.response;

import com.growup.pms.user.domain.User;
import com.growup.pms.user.domain.UserLink;
import java.util.List;
import lombok.Builder;

@Builder
public record UserResponse(String username, String email, String nickname, String bio, String profileImageUrl, List<String> links) {

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getProfile().getNickname())
                .bio(user.getProfile().getBio())
                .profileImageUrl(user.getProfile().getImage())
                .links(user.getLinks().stream().map(UserLink::getLink).toList())
                .build();
    }
}
