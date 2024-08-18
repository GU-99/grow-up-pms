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
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullable;
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

    public List<TaskResponse> getTasks(Long statusId) {
        return taskRepository.getTasksByStatusId(statusId);
    }

    public TaskDetailResponse getTask(Long taskId) {
        Task task = taskRepository.findByIdOrThrow(taskId);
        return TaskDetailResponse.of(task);
    }

    public void editTask(Long taskId, TaskEditCommand command) {
        Task task = taskRepository.findByIdOrThrow(taskId);

        changeAssignee(command.userId(), task);
        changeStatus(command.statusId(), task);
        changeTaskName(command.taskName(), task);
        changeContent(command.content(), task);
        changSortOrder(command.sortOrder(), task);
        changeStartDate(command.startDate(), task);
        changeEndDate(command.endDate(), task);
    }

    public void deleteTask(Long taskId) {

    }

    private void isValidProject(Long requestedProjectId, Long originalProjectId) {
        if (!requestedProjectId.equals(originalProjectId)) {
            throw new InvalidProjectException(ErrorCode.INVALID_PROJECT);
        }
    }

    private void changeAssignee(JsonNullable<Long> userId, Task task) {
        if (userId.isPresent()) {
            User user = userRepository.findByIdOrThrow(userId.get());
            task.editAssignee(user);
            return;
        }
        task.editAssignee(null);
    }

    private void changeStatus(JsonNullable<Long> statusId, Task task) {
        if (statusId.isPresent()) {
            Status status = statusRepository.findByIdOrThrow(statusId.get());
            task.editStatus(status);
            return;
        }
        task.editStatus(null);
    }

    private void changeTaskName(JsonNullable<String> taskName, Task task) {
        if (taskName.isPresent()) {
            task.editName(taskName.get());
        }
    }

    private void changeContent(JsonNullable<String> content, Task task) {
        if (content.isPresent()) {
            task.editContent(content.get());
            return;
        }
        task.editContent(null);
    }

    private void changSortOrder(JsonNullable<Short> sortOrder, Task task) {
        if (sortOrder.isPresent()) {
            task.editSortOrder(sortOrder.get());
        }
    }

    private void changeStartDate(JsonNullable<LocalDate> startDate, Task task) {
        if (startDate.isPresent()) {
            task.editStartDate(startDate.get());
            return;
        }
        task.editStartDate(null);
    }

    private void changeEndDate(JsonNullable<LocalDate> endDate, Task task) {
        if (endDate.isPresent()) {
            task.editEndDate(endDate.get());
            return;
        }
        task.editEndDate(null);
    }
}
