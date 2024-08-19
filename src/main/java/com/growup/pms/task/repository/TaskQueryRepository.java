package com.growup.pms.task.repository;

import com.growup.pms.task.controller.dto.response.TaskResponse;
import java.util.List;
import java.util.Map;

public interface TaskQueryRepository {

    Map<Long, List<TaskResponse>> getTasksByProjectId(Long statusId);
}
