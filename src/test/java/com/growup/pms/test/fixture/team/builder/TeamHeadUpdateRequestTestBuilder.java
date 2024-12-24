package com.growup.pms.test.fixture.team.builder;

import com.growup.pms.team.controller.dto.request.TeamHeadUpdateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamHeadUpdateRequestTestBuilder {

    private Long newHeadId = 2L;

    public static TeamHeadUpdateRequestTestBuilder 팀장_이양_요청은() {
        return new TeamHeadUpdateRequestTestBuilder();
    }

    public TeamHeadUpdateRequestTestBuilder 새로운_팀장_식별자가(Long 새로운_팀장_식별자) {
        this.newHeadId = 새로운_팀장_식별자;
        return this;
    }

    public TeamHeadUpdateRequest 이다() {
        return new TeamHeadUpdateRequest(newHeadId);
    }
}
