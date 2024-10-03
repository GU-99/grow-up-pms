package com.growup.pms.task.repository;

import com.growup.pms.task.controller.dto.response.TaskUserResponse;
import java.util.List;

public interface TaskUserQueryRepository {

    List<TaskUserResponse> getTaskUsersByProjectIdAndTaskId(Long projectId, Long taskId);
}
