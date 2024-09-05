package com.growup.pms.test.fixture.team.builder;

import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;

import com.growup.pms.team.domain.Team;
import com.growup.pms.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamTestBuilder {
    private Long id = 1L;
    private String name = "구구구";
    private String content = "안녕하세요, 구구구입니다!";
    private User creator = 사용자는().이다();

    public static TeamTestBuilder 팀은() {
        return new TeamTestBuilder();
    }

    public TeamTestBuilder 식별자가(Long 식별자) {
        this.id = 식별자;
        return this;
    }

    public TeamTestBuilder 이름이(String 이름) {
        this.name = 이름;
        return this;
    }

    public TeamTestBuilder 소개가(String 소개) {
        this.content = 소개;
        return this;
    }

    public TeamTestBuilder 팀장이(User 팀장) {
        this.creator = 팀장;
        return this;
    }

    public Team 이다() {
        var team = Team.builder()
                .name(name)
                .content(content)
                .creator(creator)
                .build();
        ReflectionTestUtils.setField(team, "id", id);
        return team;
    }
}
