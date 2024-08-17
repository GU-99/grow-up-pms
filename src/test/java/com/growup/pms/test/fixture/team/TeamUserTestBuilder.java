package com.growup.pms.test.fixture.team;

import com.growup.pms.role.domain.Role;
import com.growup.pms.team.domain.Team;
import com.growup.pms.team.domain.TeamUser;
import com.growup.pms.test.fixture.role.RoleTestBuilder;
import com.growup.pms.test.fixture.user.UserTestBuilder;
import com.growup.pms.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamUserTestBuilder {
    private Team team = TeamTestBuilder.팀은().이다();
    private User user = UserTestBuilder.사용자는().이다();
    private Role role = RoleTestBuilder.역할은().이다();
    private boolean isPendingApproval = true;

    public static TeamUserTestBuilder 팀원은() {
        return new TeamUserTestBuilder();
    }

    public TeamUserTestBuilder 팀이(Team 팀) {
        this.team = 팀;
        return this;
    }

    public TeamUserTestBuilder 사용자가(User 사용자) {
        this.user = 사용자;
        return this;
    }

    public TeamUserTestBuilder 역할이(Role 역할) {
        this.role = 역할;
        return this;
    }

    public TeamUserTestBuilder 가입_대기_여부는(boolean 가입_대기_여부) {
        this.isPendingApproval = 가입_대기_여부;
        return this;
    }

    public TeamUser 이다() {
        return TeamUser.builder()
                .team(team)
                .user(user)
                .role(role)
                .isPendingApproval(isPendingApproval)
                .build();
    }
}
