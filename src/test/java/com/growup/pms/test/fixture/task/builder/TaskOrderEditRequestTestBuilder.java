package com.growup.pms.test.fixture.task.builder;

import com.growup.pms.task.controller.dto.request.TaskOrderEditRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskOrderEditRequestTestBuilder {

    private Long taskId = 1L;
    private Long statusId = 1L;
    private Short sortOrder = 1;

    public static TaskOrderEditRequestTestBuilder 일정_순서변경_요청은() {
        return new TaskOrderEditRequestTestBuilder();
    }

    public TaskOrderEditRequestTestBuilder 일정_식별자는(Long taskId) {
        this.taskId = taskId;
        return this;
    }

    public TaskOrderEditRequestTestBuilder 상태_식별자는(Long statusId) {
        this.statusId = statusId;
        return this;
    }

    public TaskOrderEditRequestTestBuilder 정렬순서는(Short sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public TaskOrderEditRequest 이다() {
        return TaskOrderEditRequest.builder()
                .taskId(taskId)
                .statusId(statusId)
                .sortOrder(sortOrder)
                .build();
    }
}
