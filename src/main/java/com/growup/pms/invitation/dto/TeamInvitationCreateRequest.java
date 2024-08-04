package com.growup.pms.invitation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamInvitationCreateRequest {
    @NotNull
    private Long userId;

    public TeamInvitationCreateRequest(Long userId) {
        this.userId = userId;
    }

    public static TeamInvitationCreateDto toServiceDto(TeamInvitationCreateRequest request) {
        return new TeamInvitationCreateDto(request.getUserId());
    }
}
