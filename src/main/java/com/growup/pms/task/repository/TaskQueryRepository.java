package com.growup.pms.task.repository;

import com.growup.pms.task.controller.dto.response.TaskResponse;
import java.util.List;

public interface TaskQueryRepository {

    List<TaskResponse> getTasksByStatusId(Long statusId);
}
