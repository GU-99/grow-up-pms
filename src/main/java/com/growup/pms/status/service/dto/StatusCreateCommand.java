package com.growup.pms.status.service.dto;

import com.growup.pms.project.domain.Project;
import com.growup.pms.status.domain.Status;
import lombok.Builder;

@Builder
public record StatusCreateCommand(Long projectId, String statusName, String colorCode, Short sortOrder) {

    public Status toEntity(Project project) {
        return Status.builder()
                .project(project)
                .name(statusName)
                .colorCode(colorCode)
                .sortOrder(sortOrder)
                .build();
    }
}
