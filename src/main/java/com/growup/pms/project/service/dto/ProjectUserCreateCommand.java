package com.growup.pms.project.service.dto;

import com.growup.pms.project.domain.Project;
import com.growup.pms.project.domain.ProjectUser;
import com.growup.pms.role.domain.Role;
import com.growup.pms.user.domain.User;
import lombok.Builder;

@Builder
public record ProjectUserCreateCommand(
        Long userId,
        String roleName
) {
    public ProjectUser toEntity(Project project, User user, Role role) {
        return ProjectUser.builder()
                .project(project)
                .user(user)
                .role(role)
                .build();
    }
}
