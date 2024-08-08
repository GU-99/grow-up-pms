package com.growup.pms.team.service.dto;

import com.growup.pms.team.domain.Team;
import com.growup.pms.user.domain.User;
import lombok.Builder;

@Builder
public record TeamCreateCommand(String name, String content) {
    public Team toEntity(User creator) {
        return Team.builder()
                .name(name)
                .content(content)
                .creator(creator)
                .build();
    }
}
