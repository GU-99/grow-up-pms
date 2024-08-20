package com.growup.pms.user.controller.dto.response;

import lombok.Builder;

@Builder
public record UserTeamResponse(
        Long teamId,
        String name,
        String content,
        String creator,
        boolean isPendingApproval,
        String role
) {
}
