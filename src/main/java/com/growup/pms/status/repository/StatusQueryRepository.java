package com.growup.pms.status.repository;

import com.growup.pms.status.controller.dto.response.StatusResponse;
import java.util.List;

public interface StatusQueryRepository {

    List<StatusResponse> getAllStatusByProjectId(Long projectId);
}
