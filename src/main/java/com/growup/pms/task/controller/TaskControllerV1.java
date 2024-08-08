package com.growup.pms.task.controller;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.common.aop.annotation.RequirePermission;
import com.growup.pms.role.domain.PermissionType;
import com.growup.pms.status.controller.dto.response.PageResponse;
import com.growup.pms.task.controller.dto.request.TaskCreateRequest;
import com.growup.pms.task.controller.dto.request.TaskEditRequest;
import com.growup.pms.task.controller.dto.response.TaskDetailResponse;
import com.growup.pms.task.controller.dto.response.TaskResponse;
import com.growup.pms.task.service.TaskService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project/{projectId}/task")
public class TaskControllerV1 {

    private final TaskService taskService;

    @PostMapping
    @RequirePermission(PermissionType.PROJECT_STATUS_WRITE)
    public ResponseEntity<TaskDetailResponse> createTask(@PathVariable Long projectId,
                                                         @AuthenticationPrincipal SecurityUser user,
                                                         @Valid @RequestBody TaskCreateRequest request) {
        log.debug("TaskControllerV1#createTask called.");
        log.debug("projectId={}", projectId);
        log.debug("request={}", request);

        TaskDetailResponse response = taskService.createTask(request.toCommand(user.getId()));
        log.debug("response={}", response);
        String uri = UriComponentsBuilder.fromPath("/api/v1/project/{projectId}/task/{taskId}")
                .buildAndExpand(projectId, response.getTaskId())
                .toUriString();

        return ResponseEntity.created(URI.create(uri)).body(response);
    }

    @GetMapping
    @RequirePermission(PermissionType.PROJECT_TASK_READ)
    public ResponseEntity<PageResponse<List<TaskResponse>>> getTasks(@PathVariable Long projectId) {
        log.debug("TaskControllerV1#getTasks called.");
        log.debug("projectId={}", projectId);

        PageResponse<List<TaskResponse>> response = taskService.getTasks(projectId);
        log.debug("PageResponse={}", response);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{taskId}")
    @RequirePermission(PermissionType.PROJECT_TASK_READ)
    public ResponseEntity<TaskDetailResponse> getTask(@PathVariable Long projectId,
                                                      @PathVariable Long taskId) {
        log.debug("TaskControllerV1#getTask called.");
        log.debug("projectId={}", projectId);
        log.debug("taskId={}", taskId);

        TaskDetailResponse response = taskService.getTask(projectId, taskId);
        log.debug("response={}", response);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{taskId}")
    @RequirePermission(PermissionType.PROJECT_TASK_WRITE)
    public ResponseEntity<Void> editTask(@PathVariable Long projectId, @PathVariable Long taskId,
                                         @AuthenticationPrincipal SecurityUser user,
                                         @Valid @RequestBody TaskEditRequest request) {
        log.debug("TaskControllerV1#editTask called.");
        log.debug("projectId={}", projectId);
        log.debug("taskId={}", taskId);
        log.debug("request={}", request);

        taskService.editTask(request.toCommand(user.getId()));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{taskId}")
    @RequirePermission(PermissionType.PROJECT_TASK_DELETE)
    public ResponseEntity<Void> deleteTask(@PathVariable Long projectId, @PathVariable Long taskId) {
        log.debug("TaskControllerV1#deleteTask called.");
        log.debug("projectId={}", projectId);
        log.debug("taskId={}", taskId);

        taskService.deleteTask(taskId);

        return ResponseEntity.noContent().build();
    }
}
