package com.growup.pms.test.fixture.task.builder;

import static com.growup.pms.test.fixture.task.builder.TaskTestBuilder.일정은;
import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;

import com.growup.pms.task.domain.Task;
import com.growup.pms.task.domain.TaskUser;
import com.growup.pms.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskUserTestBuilder {

    private Task task = 일정은().이다();
    private User user = 사용자는().이다();

    public static TaskUserTestBuilder 일정_수행자는() {
        return new TaskUserTestBuilder();
    }

    public TaskUserTestBuilder 일정이(Task task) {
        this.task = task;
        return this;
    }

    public TaskUserTestBuilder 사용자가(User user) {
        this.user = user;
        return this;
    }

    public TaskUser 이다() {
        return TaskUser.builder()
                .task(task)
                .user(user)
                .build();
    }
}
