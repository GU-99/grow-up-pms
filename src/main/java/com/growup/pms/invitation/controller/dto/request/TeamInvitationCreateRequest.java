package com.growup.pms.invitation.controller.dto.request;

import com.growup.pms.invitation.service.dto.TeamInvitationCreateCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TeamInvitationCreateRequest(@NotNull @Positive Long userId) {
    public TeamInvitationCreateCommand toCommand() {
        return new TeamInvitationCreateCommand(userId);
    }
}
