package com.growup.pms.user.controller.dto.response;

import com.growup.pms.user.domain.User;
import com.growup.pms.user.domain.UserLink;
import java.util.List;
import java.util.Optional;
import lombok.Builder;

@Builder
public record UserUpdateResponse(
        Long userId,
        String nickname,
        String profileImageName,
        String bio,
        List<String> links
) {

    public static UserUpdateResponse of(User user) {
        return UserUpdateResponse.builder()
                .userId(user.getId())
                .bio(getOrEmptyString(user.getProfile().getBio()))
                .profileImageName(getOrEmptyString(user.getProfile().getImageName()))
                .nickname(user.getProfile().getNickname())
                .links(getLinkFromUserLink(user.getLinks()))
                .build();
    }

    private static List<String> getLinkFromUserLink(List<UserLink> userLinks) {
        return userLinks.stream().map(UserLink::getLink).toList();
    }

    private static String getOrEmptyString(String string) {
        return Optional.ofNullable(string).orElse("");
    }
}
