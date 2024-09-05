package com.growup.pms.test.fixture.project.builder;

import com.growup.pms.project.controller.dto.request.ProjectUserCreateRequest;
import com.growup.pms.role.domain.ProjectRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectUserCreateRequestTestBuilder {

    private Long userId = 1L;
    private String roleName = ProjectRole.ASSIGNEE.getRoleName();

    public static ProjectUserCreateRequestTestBuilder 프로젝트_유저_생성_요청은() {
        return new ProjectUserCreateRequestTestBuilder();
    }

    public ProjectUserCreateRequestTestBuilder 회원_식별자가(Long userId) {
        this.userId = userId;
        return this;
    }

    public ProjectUserCreateRequestTestBuilder 역할_이름이(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public ProjectUserCreateRequest 이다() {
        return ProjectUserCreateRequest.builder()
                .userId(userId)
                .roleName(roleName)
                .build();
    }
}
