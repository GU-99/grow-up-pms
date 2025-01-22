package com.growup.pms.test.fixture.project.builder;

import com.growup.pms.project.controller.dto.request.ProjectEditRequest;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectEditRequestTestBuilder {

    private String projectName = "GU-PMS 프로젝트(1년 지연)";
    private String content = "프로젝트 관리 서비스 개발 프로젝트(1년 지연)";
    private JsonNullable<LocalDate> startDate = JsonNullable.of(LocalDate.of(2023, 1, 1));
    private JsonNullable<LocalDate> endDate = JsonNullable.of(LocalDate.of(2023, 12, 31));

    public static ProjectEditRequestTestBuilder 프로젝트_수정_요청은() {
        return new ProjectEditRequestTestBuilder();
    }

    public ProjectEditRequestTestBuilder 이름은(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public ProjectEditRequestTestBuilder 설명은(String content) {
        this.content = content;
        return this;
    }

    public ProjectEditRequestTestBuilder 시작일자는(JsonNullable<LocalDate> startDate) {
        this.startDate = startDate;
        return this;
    }

    public ProjectEditRequestTestBuilder 종료일자는(JsonNullable<LocalDate> endDate) {
        this.endDate = endDate;
        return this;
    }

    public ProjectEditRequest 이다() {
        return ProjectEditRequest.builder()
                .projectName(JsonNullable.of(projectName))
                .content(JsonNullable.of(content))
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
