package com.growup.pms.task.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.InvalidProjectException;
import com.growup.pms.status.domain.Status;
import com.growup.pms.status.repository.StatusRepository;
import com.growup.pms.task.controller.dto.response.TaskDetailResponse;
import com.growup.pms.task.controller.dto.response.TaskResponse;
import com.growup.pms.task.domain.Task;
import com.growup.pms.task.repository.TaskRepository;
import com.growup.pms.task.service.dto.TaskCreateCommand;
import com.growup.pms.task.service.dto.TaskEditCommand;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
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

    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;

    @Transactional
    public TaskDetailResponse createTask(Long projectId, TaskCreateCommand command) {
        Status status = statusRepository.findById(command.statusId())
                .orElse(null);

        if (status != null) {
            isValidProject(projectId, status.getProject().getId());
        }

        User user = userRepository.findById(command.userId())
                .orElse(null);

        Task savedTask = taskRepository.save(command.toEntity(status, user));

        return TaskDetailResponse.of(savedTask);
    }

    public List<TaskResponse> getTasks(Long projectId, Long statusId) {
        return null;
    }

    public TaskDetailResponse getTask(Long projectId, Long taskId) {
        return null;
    }

    public void editTask(TaskEditCommand command) {

    }

    public void deleteTask(Long taskId) {

    }

    private void isValidProject(Long requestedProjectId, Long originalProjectId) {
        if (!requestedProjectId.equals(originalProjectId)) {
            throw new InvalidProjectException(ErrorCode.INVALID_PROJECT);
        }
    }
}
