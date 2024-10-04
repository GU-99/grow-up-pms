package com.growup.pms.project.repository;

import com.growup.pms.project.controller.dto.response.ProjectUserResponse;
import com.growup.pms.project.controller.dto.response.ProjectUserSearchResponse;
import java.util.List;

public interface ProjectUserQueryRepository {

    List<ProjectUserResponse> getProjectUsersByProjectId(Long projectId);
    List<ProjectUserSearchResponse> searchProjectUsersByNicknamePrefix(Long projectId, String nickname);
}
