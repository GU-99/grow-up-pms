package com.growup.pms.test.fixture.project.builder;

import com.growup.pms.project.controller.dto.response.ProjectUserResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectUserResponseTestBuilder {

    private Long userId = 1L;
    private String nickname = "브라운";
    private String roleName = "ADMIN";

    public static ProjectUserResponseTestBuilder 프로젝트원은() {
        return new ProjectUserResponseTestBuilder();
    }

    public ProjectUserResponseTestBuilder 회원_식별자가(Long userId) {
        this.userId = userId;
        return this;
    }

    public ProjectUserResponseTestBuilder 닉네임이(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public ProjectUserResponseTestBuilder 역할이름이(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public ProjectUserResponse 이다() {
        return ProjectUserResponse.builder()
                .userId(userId)
                .nickname(nickname)
                .roleName(roleName)
                .build();
    }
}
