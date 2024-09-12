package com.growup.pms.user.controller.dto.response;

import lombok.Builder;

@Builder
public record UserTeamResponse(
        Long teamId,
        String teamName,
        String content,
        String creator,
        Long creatorId,
        boolean isPendingApproval,
        String roleName
) {
}
