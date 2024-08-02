package com.growup.pms.test.fixture.project;

import static com.growup.pms.test.fixture.team.TeamTestBuilder.팀은;

import com.growup.pms.project.domain.Project;
import com.growup.pms.team.domain.Team;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectTestBuilder {

    private Long id = 1L;
    private Team team = 팀은().이다();
    private String name = "그로우업";
    private String content = "프로젝트 관리 서비스입니다.";
    private LocalDate startDate = LocalDate.now();
    private LocalDate endDate = LocalDate.now();

    public static ProjectTestBuilder 프로젝트는() {
        return new ProjectTestBuilder();
    }

    public ProjectTestBuilder 식별자가(Long id) {
        this.id = id;
        return this;
    }

    public ProjectTestBuilder 팀이(Team team) {
        this.team = team;
        return this;
    }

    public ProjectTestBuilder 이름이(String name) {
        this.name = name;
        return this;
    }

    public ProjectTestBuilder 설명이(String content) {
        this.content = content;
        return this;
    }

    public ProjectTestBuilder 시작일이(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public ProjectTestBuilder 종료일이(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public Project 이다() {
        var build = Project.builder()
                .team(team)
                .name(name)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        ReflectionTestUtils.setField(build, "id", id);

        return build;
    }
}
