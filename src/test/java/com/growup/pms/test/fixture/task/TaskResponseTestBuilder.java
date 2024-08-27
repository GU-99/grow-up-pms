package com.growup.pms.test.fixture.task;

import com.growup.pms.task.controller.dto.response.TaskResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskResponseTestBuilder {

    private Long taskId = 1L;
    private Long statusId = 1L;
    private String userNickname = "Hello나는팀원1";
    private String taskName = "환경설정 마치기";
    private Short sortOrder = 1;

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

    public TaskResponseTestBuilder 회원_닉네임은(String userNickname) {
        this.userNickname = userNickname;
        return this;
    }

    public TaskResponseTestBuilder 일정이름은(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public TaskResponseTestBuilder 정렬순서는(Short sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public TaskResponse 이다() {
        return TaskResponse.builder()
                .taskId(taskId)
                .statusId(statusId)
                .username(userNickname)
                .taskName(taskName)
                .sortOrder(sortOrder)
                .build();
    }
}
