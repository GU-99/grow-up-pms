package com.growup.pms.test.fixture.team.builder;

import com.growup.pms.team.controller.dto.response.TeamResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamResponseTestBuilder {
    private String teamName = "커피러버스";
    private String content = "코드 한 줄에 커피 한 잔, 우리는 카페인으로 움직이는 팀입니다!";
    private Long creatorId = 1L;

    public static TeamResponseTestBuilder 팀_생성_응답은() {
        return new TeamResponseTestBuilder();
    }

    public TeamResponseTestBuilder 이름이(String 이름) {
        this.teamName = 이름;
        return this;
    }

    public TeamResponseTestBuilder 소개가(String 소개) {
        this.content = 소개;
        return this;
    }

    public TeamResponseTestBuilder 생성자_식별자가(Long 생성자_식별자) {
        this.creatorId = 생성자_식별자;
        return this;
    }

    public TeamResponse 이다() {
        return TeamResponse.builder()
                .teamName(teamName)
                .content(content)
                .creatorId(creatorId)
                .build();
    }
}
