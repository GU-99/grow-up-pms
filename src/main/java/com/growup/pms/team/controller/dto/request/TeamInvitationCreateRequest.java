package com.growup.pms.team.controller.dto.request;

import com.growup.pms.team.service.dto.TeamInvitationCreateCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TeamInvitationCreateRequest(
        @NotNull
        @Positive
        Long userId,

        @NotNull
        @Size(min = 2, max = 20)
        String roleName
) {
    public TeamInvitationCreateCommand toCommand() {
        return new TeamInvitationCreateCommand(userId, roleName);
    }
}
