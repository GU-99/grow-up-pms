package com.growup.pms.user.service.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record UserUpdateCommand(
        String nickname,
        String bio,
        String profileImageUrl,
        List<String> links
) {
}
