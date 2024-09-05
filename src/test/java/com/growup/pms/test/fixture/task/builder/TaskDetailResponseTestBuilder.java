package com.growup.pms.test.fixture.task.builder;

import com.growup.pms.task.controller.dto.response.TaskDetailResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskDetailResponseTestBuilder {

    private Long taskId = 1L;
    private Long statusId = 1L;
    private String taskName = "환경설정 마치기";
    private String content = "# GU-PMS 에 필요한 환경 설정은 다음과 같습니다. <br> ## 목차 <br> ### 1. JPA 의존성 주입";
    private Short sortOrder = 1;
    private String startDate = "2023-01-01";
    private String endDate = "2023-12-31";

    public static TaskDetailResponseTestBuilder 일정_상세조회_응답은() {
        return new TaskDetailResponseTestBuilder();
    }

    public TaskDetailResponseTestBuilder 일정_식별자는(Long taskId) {
        this.taskId = taskId;
        return this;
    }

    public TaskDetailResponseTestBuilder 상태_식별자는(Long statusId) {
        this.statusId = statusId;
        return this;
    }

    public TaskDetailResponseTestBuilder 일정이름은(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public TaskDetailResponseTestBuilder 본문내용은(String content) {
        this.content = content;
        return this;
    }

    public TaskDetailResponseTestBuilder 정렬순서는(Short sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public TaskDetailResponseTestBuilder 시작일자는(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public TaskDetailResponseTestBuilder 종료일자는(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public TaskDetailResponse 이다() {
        return TaskDetailResponse.builder()
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
