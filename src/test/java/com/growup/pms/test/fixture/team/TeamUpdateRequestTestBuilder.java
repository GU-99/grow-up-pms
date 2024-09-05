package com.growup.pms.test.fixture.team;

import com.growup.pms.team.controller.dto.request.TeamUpdateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamUpdateRequestTestBuilder {
    private String teamName = "구구구";
    private String content = "안녕하세요, 구구구입니다!";

    public static TeamUpdateRequestTestBuilder 팀_변경_요청은() {
        return new TeamUpdateRequestTestBuilder();
    }

    public TeamUpdateRequestTestBuilder 이름이(String 이름) {
        this.teamName = 이름;
        return this;
    }

    public TeamUpdateRequestTestBuilder 소개가(String 소개) {
        this.content = 소개;
        return this;
    }

    public TeamUpdateRequest 이다() {
        return TeamUpdateRequest.builder()
                .teamName(JsonNullable.of(teamName))
                .content(JsonNullable.of(content))
                .build();
    }
}
