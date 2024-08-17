package com.growup.pms.invitation.service.dto;

import com.growup.pms.invitation.domian.TeamInvitation;
import com.growup.pms.team.domain.Team;
import com.growup.pms.user.domain.User;

public record TeamInvitationCreateCommand(Long userId) {
    public static TeamInvitation toEntity(User user, Team team) {
        return TeamInvitation.builder()
                .user(user)
                .team(team)
                .build();
    }
}
