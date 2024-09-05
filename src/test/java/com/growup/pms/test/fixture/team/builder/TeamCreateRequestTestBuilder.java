package com.growup.pms.test.fixture.team.builder;

import static com.growup.pms.test.fixture.team.builder.TeamCreateRequestTestBuilder.TeamCoworkerRequestTestBuilder.초대된_사용자는;

import com.growup.pms.team.controller.dto.request.TeamCreateRequest;
import com.growup.pms.team.controller.dto.request.TeamCreateRequest.TeamCoworkerRequest;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamCreateRequestTestBuilder {
    private String teamName = "커피러버스";
    private String content = "코드 한 줄에 커피 한 잔, 우리는 카페인으로 움직이는 팀입니다!";
    private List<TeamCoworkerRequest> coworkers = List.of(초대된_사용자는().이다());

    public static TeamCreateRequestTestBuilder 팀_생성_요청은() {
        return new TeamCreateRequestTestBuilder();
    }

    public TeamCreateRequestTestBuilder 이름이(String 이름) {
        this.teamName = 이름;
        return this;
    }

    public TeamCreateRequestTestBuilder 소개가(String 소개) {
        this.content = 소개;
        return this;
    }

    public TeamCreateRequestTestBuilder 초대된_사용자가(List<TeamCoworkerRequest> 초대된_사용자) {
        this.coworkers = 초대된_사용자;
        return this;
    }

    public TeamCreateRequest 이다() {
        return TeamCreateRequest.builder()
                .teamName(teamName)
                .content(content)
                .coworkers(coworkers)
                .build();
    }

    public static class TeamCoworkerRequestTestBuilder {
        private Long userId = 2L;
        private String roleName = "Mate";

        public static TeamCoworkerRequestTestBuilder 초대된_사용자는() {
            return new TeamCoworkerRequestTestBuilder();
        }

        public TeamCoworkerRequestTestBuilder 식별자가(Long 식별자) {
            this.userId = 식별자;
            return this;
        }

        public TeamCoworkerRequestTestBuilder 역할이(String 역할) {
            this.roleName = 역할;
            return this;
        }

        public TeamCoworkerRequest 이다() {
            return TeamCoworkerRequest.builder()
                    .userId(userId)
                    .roleName(roleName)
                    .build();
        }
    }
}
