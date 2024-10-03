package com.growup.pms.test.fixture.task.builder;

import com.growup.pms.task.controller.dto.response.TaskUserResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskUserResponseTestBuilder {

    private Long userId;
    private String nickname;
    private String roleName;

    public static TaskUserResponseTestBuilder 일정_수행자_목록_응답은() {
        return new TaskUserResponseTestBuilder();
    }

    public TaskUserResponseTestBuilder 회원_식별자가(Long userId) {
        this.userId = userId;
        return this;
    }

    public TaskUserResponseTestBuilder 닉네임이(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public TaskUserResponseTestBuilder 역할_이름이(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public TaskUserResponse 이다() {
        return TaskUserResponse.builder()
                .userId(userId)
                .nickname(nickname)
                .roleName(roleName)
                .build();
    }
}
