package com.growup.pms.task.repository;

import com.growup.pms.task.controller.dto.response.TaskAttachmentResponse;
import java.util.List;

public interface TaskAttachmentQueryRepository {

    List<TaskAttachmentResponse> getTaskAttachmentsByTaskId(Long taskId);
}
