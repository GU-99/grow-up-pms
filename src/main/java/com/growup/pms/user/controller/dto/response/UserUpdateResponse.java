package com.growup.pms.user.controller.dto.response;

import com.growup.pms.user.domain.User;
import java.util.List;
import lombok.Builder;

@Builder
public record UserUpdateResponse(
        Long userId,
        String nickname,
        String imageUrl,
        String bio,
        List<String> links
) {

    public static UserUpdateResponse of(User user, List<String> links) {
        return UserUpdateResponse.builder()
                .userId(user.getId())
                .bio(user.getProfile().getBio())
                .imageUrl(user.getProfile().getImage())
                .nickname(user.getProfile().getNickname())
                .links(links)
                .build();
    }
}
