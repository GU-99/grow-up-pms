package com.growup.pms.test.fixture.team.builder;

import com.growup.pms.team.controller.dto.response.TeamUserSearchResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamUserSearchResponseTestBuilder {
    private Long userId = 1L;
    private String nickname = "브라운";

    public static TeamUserSearchResponseTestBuilder 팀원_검색_응답은() {
        return new TeamUserSearchResponseTestBuilder();
    }

    public TeamUserSearchResponseTestBuilder 사용자_식별자가(Long 사용자_식별자) {
        this.userId = 사용자_식별자;
        return this;
    }

    public TeamUserSearchResponseTestBuilder 닉네임이(String 닉네임) {
        this.nickname = 닉네임;
        return this;
    }

    public TeamUserSearchResponse 이다() {
        return TeamUserSearchResponse.builder()
                .userId(userId)
                .nickname(nickname)
                .build();
    }
}
