package com.growup.pms.test.fixture.user;

import com.growup.pms.user.controller.dto.response.UserTeamResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserTeamResponseTestBuilder {
    private Long teamId = 1L;
    private String name = "팀 이름";
    private String content = "팀 소개";
    private String creator = "팀장 닉네임";
    private boolean isPendingApproval = true;

    public static UserTeamResponseTestBuilder 가입한_팀_응답은() {
        return new UserTeamResponseTestBuilder();
    }

    public UserTeamResponseTestBuilder 팀_식별자가(Long 팀_식별자) {
        this.teamId = 팀_식별자;
        return this;
    }

    public UserTeamResponseTestBuilder 팀_이름이(String 팀_이름) {
        this.name = 팀_이름;
        return this;
    }

    public UserTeamResponseTestBuilder 팀_소개가(String 팀_소개) {
        this.content = 팀_소개;
        return this;
    }

    public UserTeamResponseTestBuilder 팀장_닉네임이(String 팀장_닉네임) {
        this.creator = 팀장_닉네임;
        return this;
    }

    public UserTeamResponseTestBuilder 가입_대기_여부가(boolean 가입_대기_여부) {
        this.isPendingApproval = 가입_대기_여부;
        return this;
    }

    public UserTeamResponse 이다() {
        return new UserTeamResponse(teamId, name, content, creator, isPendingApproval);
    }
}
