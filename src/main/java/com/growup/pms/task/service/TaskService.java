package com.growup.pms.task.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
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
import java.util.Map;
import java.util.function.BiConsumer;
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
        Status status = statusRepository.findByIdOrThrow(command.statusId());

        isValidProject(projectId, status.getProject().getId());

        User user = userRepository.findById(command.userId())
                .orElse(null);

        Task savedTask = taskRepository.save(command.toEntity(status, user));

        return TaskDetailResponse.of(savedTask);
    }

    public Map<Long, List<TaskResponse>> getTasks(Long projectId) {
        // TODO: Project 의 유무에 대한 검증 로직 추가 여부에 대해서 논의 필요
        return taskRepository.getTasksByProjectId(projectId);
    }

    public TaskDetailResponse getTask(Long taskId) {
        Task task = taskRepository.findByIdOrThrow(taskId);
        return TaskDetailResponse.of(task);
    }

    @Transactional
    public void editTask(Long taskId, TaskEditCommand command) {
        Task task = taskRepository.findByIdOrThrow(taskId);

        editField(command.userId(), this::changeAssignee, task);
        editField(command.statusId(), this::changeStatus, task);
        editField(command.taskName(), (v, t) -> t.editName(v.get()), task);
        editField(command.content(), (v, t) -> t.editContent(v.get()), task);
        editField(command.sortOrder(), (v, t) -> t.editSortOrder(v.get()), task);
        editField(command.startDate(), (v, t) -> t.editStartDate(v.get()), task);
        editField(command.endDate(), (v, t) -> t.editEndDate(v.get()), task);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findByIdOrThrow(taskId);
        taskRepository.delete(task);
    }

    private void isValidProject(Long requestedProjectId, Long originalProjectId) {
        if (!requestedProjectId.equals(originalProjectId)) {
            throw new BusinessException(ErrorCode.INVALID_PROJECT);
        }
    }

    private <T> void editField(JsonNullable<T> value, BiConsumer<JsonNullable<T>, Task> updater, Task task) {
        value.ifPresent(v -> updater.accept(JsonNullable.of(v), task));
    }

    private void changeAssignee(JsonNullable<Long> userId, Task task) {
        userId.ifPresent(id -> {
            User user = userRepository.findByIdOrThrow(id);
            task.editAssignee(user);
        });
    }

    private void changeStatus(JsonNullable<Long> statusId, Task task) {
        statusId.ifPresent(id -> {
            Status status = statusRepository.findByIdOrThrow(id);
            task.editStatus(status);
        });
    }
}
