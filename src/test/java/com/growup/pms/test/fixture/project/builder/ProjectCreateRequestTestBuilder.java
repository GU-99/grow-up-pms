package com.growup.pms.test.fixture.project.builder;

import com.growup.pms.project.controller.dto.request.ProjectCreateRequest;
import com.growup.pms.project.controller.dto.request.ProjectUserCreateRequest;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectCreateRequestTestBuilder {

    private String projectName = "GU-PMS 프로젝트";
    private String content = "프로젝트 관리 서비스 개발 프로젝트";
    private LocalDate startDate = LocalDate.of(2024, 1, 1);
    private LocalDate endDate = LocalDate.of(2024, 12, 31);
    private List<ProjectUserCreateRequest> coworkers = Collections.emptyList();

    public static ProjectCreateRequestTestBuilder 프로젝트_생성_요청은() {
        return new ProjectCreateRequestTestBuilder();
    }

    public ProjectCreateRequestTestBuilder 프로젝트_이름은(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public ProjectCreateRequestTestBuilder 프로젝트_설명은(String content) {
        this.content = content;
        return this;
    }

    public ProjectCreateRequestTestBuilder 시작일자는(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public ProjectCreateRequestTestBuilder 종료일자는(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public ProjectCreateRequestTestBuilder 초대할_팀원들은(List<ProjectUserCreateRequest> coworkers) {
        this.coworkers = coworkers;
        return this;
    }

    public ProjectCreateRequest 이다() {
        return ProjectCreateRequest.builder()
                .projectName(projectName)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .coworkers(coworkers)
                .build();
    }
}
