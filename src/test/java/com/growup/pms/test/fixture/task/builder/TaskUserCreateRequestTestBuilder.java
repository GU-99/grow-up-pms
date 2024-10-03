package com.growup.pms.test.fixture.task.builder;

import com.growup.pms.task.controller.dto.request.TaskUserCreateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskUserCreateRequestTestBuilder {

    private Long userId = 1L;

    public static TaskUserCreateRequestTestBuilder 일정_수행자_추가_요청은() {
        return new TaskUserCreateRequestTestBuilder();
    }

    public TaskUserCreateRequestTestBuilder 회원_식별자는(Long userId) {
        this.userId = userId;
        return this;
    }

    public TaskUserCreateRequest 이다() {
        return TaskUserCreateRequest.builder()
                .userId(userId)
                .build();
    }
}
