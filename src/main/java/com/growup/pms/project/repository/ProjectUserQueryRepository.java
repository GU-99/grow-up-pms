package com.growup.pms.project.repository;

import com.growup.pms.project.controller.dto.response.ProjectUserResponse;
import java.util.List;

public interface ProjectUserQueryRepository {

    List<ProjectUserResponse> getProjectUsersByProjectId(Long projectId);
}
