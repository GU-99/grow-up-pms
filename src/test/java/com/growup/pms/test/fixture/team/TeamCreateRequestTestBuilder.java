package com.growup.pms.test.fixture.team;

import com.growup.pms.team.controller.dto.request.TeamCreateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamCreateRequestTestBuilder {
    private String name = "구구구";
    private String content = "안녕하세요, 구구구입니다!";

    public static TeamCreateRequestTestBuilder 팀_생성_요청은() {
        return new TeamCreateRequestTestBuilder();
    }

    public TeamCreateRequestTestBuilder 이름이(String 이름) {
        this.name = 이름;
        return this;
    }

    public TeamCreateRequestTestBuilder 소개가(String 소개) {
        this.content = 소개;
        return this;
    }

    public TeamCreateRequest 이다() {
        return TeamCreateRequest.builder()
                .name(name)
                .content(content)
                .build();
    }
}
