package com.growup.pms.team.service.dto;

import com.growup.pms.team.domain.Team;
import com.growup.pms.user.domain.User;
import java.util.List;
import lombok.Builder;

@Builder
public record TeamCreateCommand(String name, String content, Long creatorId, List<TeamCoworkerCommand> coworkers) {
    public Team toEntity(User creator) {
        return Team.builder()
                .name(name)
                .content(content)
                .creator(creator)
                .build();
    }

    @Builder
    public record TeamCoworkerCommand(Long id, String role) {
    }
}
