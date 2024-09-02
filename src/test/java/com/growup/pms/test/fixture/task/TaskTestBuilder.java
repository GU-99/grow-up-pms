package com.growup.pms.test.fixture.task;

import com.growup.pms.status.domain.Status;
import com.growup.pms.task.domain.Task;
import com.growup.pms.test.fixture.status.StatusTestBuilder;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskTestBuilder {

    private Long id = 1L;
    private Status status = StatusTestBuilder.상태는().이다();
    private String name = "환경설정 마치기";
    private String content = "# GU-PMS 에 필요한 환경 설정은 다음과 같습니다. <br> ## 목차 <br> ### 1. JPA 의존성 주입";
    private Short sortOrder = 1;
    private LocalDate startDate = LocalDate.parse("2023-01-01");
    private LocalDate endDate = LocalDate.parse("2023-12-31");

    public static TaskTestBuilder 일정은() {
        return new TaskTestBuilder();
    }

    public TaskTestBuilder 식별자는(Long id) {
        this.id = id;
        return this;
    }

    public TaskTestBuilder 상태는(Status status) {
        this.status = status;
        return this;
    }

    public TaskTestBuilder 이름은(String name) {
        this.name = name;
        return this;
    }

    public TaskTestBuilder 내용은(String content) {
        this.content = content;
        return this;
    }

    public TaskTestBuilder 정렬순서는(Short sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public TaskTestBuilder 시작일자는(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public TaskTestBuilder 종료일자는(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public Task 이다() {

        var build = Task.builder()
                .status(status)
                .name(name)
                .content(content)
                .sortOrder(sortOrder)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        ReflectionTestUtils.setField(build, "id", id);
        return build;
    }
}
