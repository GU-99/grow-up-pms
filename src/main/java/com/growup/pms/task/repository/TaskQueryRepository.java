package com.growup.pms.task.repository;

import com.growup.pms.task.controller.dto.response.TaskResponse;
import java.util.List;

public interface TaskQueryRepository {

    void decreaseSortOrderByStatus(Long statusId, Short sortOrder);

    List<TaskResponse> getAllTasksByStatus(Long statusId);
}
