package com.growup.pms.test.fixture.team;

import com.growup.pms.invitation.domian.TeamInvitation;
import com.growup.pms.test.fixture.user.UserTestBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamInvitationTestBuilder {
    private Long id = 1L;
    private Long teamId = 1L;
    private Long userId = 1L;

    public static TeamInvitationTestBuilder 팀_초대는() {
        return new TeamInvitationTestBuilder();
    }

    public TeamInvitationTestBuilder 식별자가(Long 식별자) {
        this.id = 식별자;
        return this;
    }

    public TeamInvitationTestBuilder 팀_식별자가(Long 팀_식별자) {
        this.teamId = 팀_식별자;
        return this;
    }

    public TeamInvitationTestBuilder 초대할_사용자_식별자가(Long 초대할_사용자_식별자) {
        this.userId = 초대할_사용자_식별자;
        return this;
    }

    public TeamInvitation 이다() {
        var teamInvitation = TeamInvitation.builder()
                .user(UserTestBuilder.사용자는().식별자가(userId).이다())
                .team(TeamTestBuilder.팀은().식별자가(teamId).이다())
                .build();
        ReflectionTestUtils.setField(teamInvitation, "id", id);
        return teamInvitation;
    }
}
