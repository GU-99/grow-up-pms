package com.growup.pms.test.fixture.task.builder;

import static com.growup.pms.test.fixture.task.builder.TaskResponseTestBuilder.일정_전체조회_응답은;

import com.growup.pms.task.controller.dto.response.TaskKanbanResponse;
import com.growup.pms.task.controller.dto.response.TaskResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskKanbanResponseTestBuilder {
    private Long statusId = 1L;
    private String statusName = "대기중";
    private String colorCode = "#FFFFFF";
    private Short sortOrder = (short) 1;
    private List<TaskResponse> tasks = List.of(일정_전체조회_응답은().이다());

    public static TaskKanbanResponseTestBuilder 일정_칸반_응답은() {
        return new TaskKanbanResponseTestBuilder();
    }

    public TaskKanbanResponseTestBuilder 상태_식별자는(Long statusId) {
        this.statusId = statusId;
        return this;
    }

    public TaskKanbanResponseTestBuilder 상태_이름은(String statusName) {
        this.statusName = statusName;
        return this;
    }

    public TaskKanbanResponseTestBuilder 색상코드는(String colorCode) {
        this.colorCode = colorCode;
        return this;
    }

    public TaskKanbanResponseTestBuilder 정렬순서는(Short sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public TaskKanbanResponseTestBuilder 일정목록은(List<TaskResponse> tasks) {
        this.tasks = tasks;
        return this;
    }

    public TaskKanbanResponse 이다() {
        return TaskKanbanResponse.builder()
                .statusId(statusId)
                .statusName(statusName)
                .colorCode(colorCode)
                .sortOrder(sortOrder)
                .tasks(tasks)
                .build();
    }
}
