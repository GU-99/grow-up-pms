package com.growup.pms.test.fixture.task;

import com.growup.pms.task.controller.dto.request.TaskCreateRequest;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskCreateRequestTestBuilder {

    private Long statusId = 1L;
    private List<Long> assigneeIds = List.of(1L, 2L, 3L);
    private String taskName = "환경설정 마치기";
    private String content = "# GU-PMS 에 필요한 환경 설정은 다음과 같습니다. <br> ## 목차 <br> ### 1. JPA 의존성 주입";
    private Short sortOrder = 1;
    private LocalDate startDate = LocalDate.of(2023, 1, 1);
    private LocalDate endDate = LocalDate.of(2023, 12, 31);

    public static TaskCreateRequestTestBuilder 일정_생성_요청은() {
        return new TaskCreateRequestTestBuilder();
    }

    public TaskCreateRequestTestBuilder 상태_식별자는(Long statusId) {
        this.statusId = statusId;
        return this;
    }

    public TaskCreateRequestTestBuilder 담당자_ID_목록은(List<Long> assigneeIds) {
        this.assigneeIds = assigneeIds;
        return this;
    }

    public TaskCreateRequestTestBuilder 일정이름은(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public TaskCreateRequestTestBuilder 본문내용은(String content) {
        this.content = content;
        return this;
    }

    public TaskCreateRequestTestBuilder 정렬순서는(Short sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public TaskCreateRequestTestBuilder 시작일자는(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public TaskCreateRequestTestBuilder 종료일자는(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public TaskCreateRequest 이다() {
        return TaskCreateRequest.builder()
                .statusId(statusId)
                .assigneeIds(assigneeIds)
                .taskName(taskName)
                .content(content)
                .sortOrder(sortOrder)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
