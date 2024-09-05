package com.growup.pms.test.fixture.project.builder;

import com.growup.pms.project.controller.dto.response.ProjectResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectResponseTestBuilder {

    private Long projectId = 1L;
    private String projectName = "PMS 프로젝트";
    private String content = "프로젝트 관리 서비스를 개발하는 프로젝트";
    private LocalDate startDate = LocalDate.of(2024, 1, 1);
    private LocalDate endDate = LocalDate.of(2024, 12, 31);
    private LocalDateTime createdAt = LocalDateTime.of(2024, 8, 31, 22, 30, 0);
    private LocalDateTime updatedAt = LocalDateTime.of(2024, 8, 31, 22, 30, 0);

    public static ProjectResponseTestBuilder 프로젝트_목록조회_응답은() {
        return new ProjectResponseTestBuilder();
    }

    public ProjectResponseTestBuilder 프로젝트_식별자는(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public ProjectResponseTestBuilder 프로젝트_이름은(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public ProjectResponseTestBuilder 프로젝트_설명은(String content) {
        this.content = content;
        return this;
    }

    public ProjectResponseTestBuilder 시작일자는(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public ProjectResponseTestBuilder 종료일자는(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public ProjectResponseTestBuilder 생성시점은(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ProjectResponseTestBuilder 변경일자는(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public ProjectResponse 이다() {
        return ProjectResponse.builder()
                .projectId(projectId)
                .projectName(projectName)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
