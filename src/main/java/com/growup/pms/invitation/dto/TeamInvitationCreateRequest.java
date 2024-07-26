package com.growup.pms.invitation.dto;

import com.growup.pms.invitation.domian.TeamInvitation;
import com.growup.pms.team.domain.Team;
import com.growup.pms.user.domain.User;
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

    public static TeamInvitation toEntity(User user, Team team) {
        return TeamInvitation.builder()
                .user(user)
                .team(team)
                .build();
    }
}
