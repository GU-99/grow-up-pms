package com.growup.pms.team.dto;

import com.growup.pms.team.domain.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamResponse {
    private String name;
    private String content;

    @Builder
    public TeamResponse(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public static TeamResponse from(Team team) {
        return TeamResponse.builder()
                .name(team.getName())
                .content(team.getContent())
                .build();
    }
}
