package com.growup.pms.invitation.controller.dto.request;

import com.growup.pms.invitation.domian.dto.TeamInvitationCreateCommand;
import jakarta.validation.constraints.NotNull;

public record TeamInvitationCreateRequest(@NotNull Long userId) {
    public TeamInvitationCreateCommand toCommand() {
        return new TeamInvitationCreateCommand(userId);
    }
}
