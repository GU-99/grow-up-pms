package com.growup.pms.project.repository;

import com.growup.pms.project.controller.dto.response.ProjectResponse;
import java.util.List;

public interface ProjectQueryRepository {

    List<ProjectResponse> getProjectsByTeamId(Long teamId);
}
