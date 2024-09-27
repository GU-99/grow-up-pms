package com.growup.pms.test.fixture.team.builder;

import com.growup.pms.role.domain.TeamRole;
import com.growup.pms.team.controller.dto.response.TeamUserResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamUserResponseTestBuilder {
    private Long userId = 1L;
    private String nickname = "브라운";
    private String roleName = TeamRole.HEAD.getRoleName();

    public static TeamUserResponseTestBuilder 팀원_응답은() {
        return new TeamUserResponseTestBuilder();
    }

    public TeamUserResponseTestBuilder 사용자_식별자가(Long 사용자_식별자) {
        this.userId = 사용자_식별자;
        return this;
    }

    public TeamUserResponseTestBuilder 닉네임이(String 닉네임) {
        this.nickname = 닉네임;
        return this;
    }

    public TeamUserResponseTestBuilder 역할명이(String 역할명) {
        this.roleName = 역할명;
        return this;
    }

    public TeamUserResponse 이다() {
        return TeamUserResponse.builder()
                .userId(userId)
                .nickname(nickname)
                .roleName(roleName)
                .build();
    }
}
