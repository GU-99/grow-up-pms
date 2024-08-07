package com.growup.pms.task.service;

import com.growup.pms.status.controller.dto.response.PageResponse;
import com.growup.pms.task.controller.dto.response.TaskDetailResponse;
import com.growup.pms.task.controller.dto.response.TaskResponse;
import com.growup.pms.task.service.dto.TaskCreateDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    public TaskDetailResponse createTask(TaskCreateDto dto) {
        return null;
    }

    public PageResponse<List<TaskResponse>> getTasks(Long projectId, Long userId) {
        return null;
    }

    public TaskDetailResponse getTask(Long projectId, Long taskId) {
        return null;
    }
}
