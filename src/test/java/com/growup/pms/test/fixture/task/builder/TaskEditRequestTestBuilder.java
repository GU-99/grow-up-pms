package com.growup.pms.test.fixture.task.builder;

import com.growup.pms.task.controller.dto.request.TaskEditRequest;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskEditRequestTestBuilder {

    private Long statusId = 1L;
    private String taskName = "환경설정 마치기";
    private String content = "# GU-PMS 에 필요한 환경 설정은 다음과 같습니다. <br> ## 목차 <br> ### 1. JPA 의존성 주입";
    private LocalDate startDate = LocalDate.of(2023, 1, 1);
    private LocalDate endDate = LocalDate.of(2023, 12, 31);

    public static TaskEditRequestTestBuilder 일정_수정_요청은() {
        return new TaskEditRequestTestBuilder();
    }

    public TaskEditRequestTestBuilder 상태_식별자는(Long statusId) {
        this.statusId = statusId;
        return this;
    }

    public TaskEditRequestTestBuilder 일정이름은(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public TaskEditRequestTestBuilder 본문내용은(String content) {
        this.content = content;
        return this;
    }

    public TaskEditRequestTestBuilder 시작일자는(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public TaskEditRequestTestBuilder 종료일자는(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public TaskEditRequest 이다() {
        return TaskEditRequest.builder()
                .statusId(JsonNullable.of(statusId))
                .taskName(JsonNullable.of(taskName))
                .content(JsonNullable.of(content))
                .startDate(JsonNullable.of(startDate))
                .endDate(JsonNullable.of(endDate))
                .build();
    }
}
