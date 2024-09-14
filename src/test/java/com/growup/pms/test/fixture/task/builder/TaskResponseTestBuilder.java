package com.growup.pms.test.fixture.task.builder;

import com.growup.pms.task.controller.dto.response.TaskResponse;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskResponseTestBuilder {

    private Long taskId = 1L;
    private Long statusId = 1L;
    private String taskName = "환경설정 마치기";
    private String content = "# GU-PMS 에 필요한 환경 설정은 다음과 같습니다. <br> ## 목차 <br> ### 1. JPA 의존성 주입";
    private Short sortOrder = 1;
    private LocalDate startDate = LocalDate.of(2023,1,1);
    private LocalDate endDate = LocalDate.of(2023,1,15);

    public static TaskResponseTestBuilder 일정_전체조회_응답은() {
        return new TaskResponseTestBuilder();
    }

    public TaskResponseTestBuilder 일정_식별자는(Long taskId) {
        this.taskId = taskId;
        return this;
    }

    public TaskResponseTestBuilder 상태_식별자는(Long statusId) {
        this.statusId = statusId;
        return this;
    }

    public TaskResponseTestBuilder 일정이름은(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public TaskResponseTestBuilder 일정내용은(String content) {
        this.content = content;
        return this;
    }

    public TaskResponseTestBuilder 정렬순서는(Short sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public TaskResponseTestBuilder 시작일자는(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public TaskResponseTestBuilder 종료일자는(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public TaskResponse 이다() {
        return TaskResponse.builder()
                .taskId(taskId)
                .statusId(statusId)
                .taskName(taskName)
                .content(content)
                .sortOrder(sortOrder)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
