package com.growup.pms.test.fixture.task.builder;

import static com.growup.pms.test.fixture.task.builder.TaskOrderEditRequestTestBuilder.일정_순서변경_요청은;

import com.growup.pms.task.controller.dto.request.TaskOrderEditRequest;
import com.growup.pms.task.controller.dto.request.TaskOrderListEditRequest;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskOrderListEditRequestTestBuilder {

    private List<TaskOrderEditRequest> tasks = List.of(일정_순서변경_요청은().이다());

    public static TaskOrderListEditRequestTestBuilder 일정_순서변경_목록_요청은() {
        return new TaskOrderListEditRequestTestBuilder();
    }

    public TaskOrderListEditRequestTestBuilder 일정_목록은(List<TaskOrderEditRequest> tasks) {
        this.tasks = tasks;
        return this;
    }

    public TaskOrderListEditRequest 이다() {
        return TaskOrderListEditRequest.builder()
                .tasks(tasks)
                .build();
    }
}
