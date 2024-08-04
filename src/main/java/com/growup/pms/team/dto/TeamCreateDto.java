package com.growup.pms.team.dto;

import com.growup.pms.team.domain.Team;
import com.growup.pms.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamCreateDto {
    private String name;
    private String content;

    @Builder
    public TeamCreateDto(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public static Team toEntity(TeamCreateDto request, User creator) {
        return Team.builder()
                .name(request.getName())
                .content(request.getContent())
                .creator(creator)
                .build();
    }
}
