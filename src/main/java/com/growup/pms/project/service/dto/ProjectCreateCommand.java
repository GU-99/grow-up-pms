package com.growup.pms.project.service.dto;

import com.growup.pms.project.domain.Project;
import com.growup.pms.team.domain.Team;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record ProjectCreateCommand(
        String projectName,
        String content,
        LocalDate startDate,
        LocalDate endDate,
        List<ProjectUserCreateCommand> coworkers
) {
    public Project toEntity(Team team) {
        return Project.builder()
                .team(team)
                .name(projectName)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
