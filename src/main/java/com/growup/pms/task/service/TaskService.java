package com.growup.pms.task.service;

import com.growup.pms.task.controller.dto.response.TaskDetailResponse;
import com.growup.pms.task.controller.dto.response.TaskResponse;
import com.growup.pms.task.service.dto.TaskCreateCommand;
import com.growup.pms.task.service.dto.TaskEditCommand;
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

    public TaskDetailResponse createTask(TaskCreateCommand command) {
        return null;
    }

    public List<TaskResponse> getTasks(Long projectId) {
        return null;
    }

    public TaskDetailResponse getTask(Long projectId, Long taskId) {
        return null;
    }

    public void editTask(TaskEditCommand command) {

    }

    public void deleteTask(Long taskId) {

    }
}
