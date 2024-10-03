package com.growup.pms.test.fixture.project.builder;

import com.growup.pms.project.controller.dto.request.ProjectRoleEditRequest;
import com.growup.pms.role.domain.ProjectRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectRoleEditRequestTestBuilder {

    private String roleName = ProjectRole.LEADER.getRoleName();

    public static ProjectRoleEditRequestTestBuilder 프로젝트원_역할_변경_요청은() {
        return new ProjectRoleEditRequestTestBuilder();
    }

    public ProjectRoleEditRequestTestBuilder 역할이름은(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public ProjectRoleEditRequest 이다() {
        return ProjectRoleEditRequest
                .builder()
                .roleName(roleName)
                .build();
    }
}
