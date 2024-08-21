package com.growup.pms.task.controller;

import com.growup.pms.common.aop.annotation.ProjectId;
import com.growup.pms.common.aop.annotation.RequirePermission;
import com.growup.pms.role.domain.PermissionType;
import com.growup.pms.task.controller.dto.request.TaskCreateRequest;
import com.growup.pms.task.controller.dto.request.TaskEditRequest;
import com.growup.pms.task.controller.dto.response.TaskDetailResponse;
import com.growup.pms.task.controller.dto.response.TaskResponse;
import com.growup.pms.task.service.TaskService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<TaskDetailResponse> createTask(@ProjectId @PathVariable Long projectId,
                                                         @Valid @RequestBody TaskCreateRequest request) {
        log.debug("TaskControllerV1#createTask called.");
        log.debug("일정 생성을 위한 projectId={}", projectId);
        log.debug("일정 생성을 위한 TaskCreateRequest={}", request);

        TaskDetailResponse response = taskService.createTask(projectId, request.toCommand());
        log.debug("생성된 일정에 대한 TaskDetailResponse={}", response);

        String uri = UriComponentsBuilder.fromPath("/api/v1/project/{projectId}/task/{taskId}")
                .buildAndExpand(projectId, response.getTaskId())
                .toUriString();

        return ResponseEntity.created(URI.create(uri)).body(response);
    }

    @GetMapping
    @RequirePermission(PermissionType.PROJECT_TASK_READ)
    public ResponseEntity<Map<Long, List<TaskResponse>>> getTasks(@ProjectId @PathVariable Long projectId) {
        log.debug("TaskControllerV1#getTasks called.");
        log.debug("일정 전체 조회를 위한 projectId={}", projectId);

        Map<Long, List<TaskResponse>> response = taskService.getTasks(projectId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{taskId}")
    @RequirePermission(PermissionType.PROJECT_TASK_READ)
    public ResponseEntity<TaskDetailResponse> getTask(@ProjectId @PathVariable Long projectId,
                                                      @PathVariable Long taskId) {
        log.debug("TaskControllerV1#getTask called.");
        log.debug("일정 상세 조회를 위한 projectId={}", projectId);
        log.debug("일정 상세 조회를 위한 taskId={}", taskId);

        TaskDetailResponse response = taskService.getTask(taskId);
        log.debug("일정 상세 조회 결과 TaskDetailResponse={}", response);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{taskId}")
    @RequirePermission(PermissionType.PROJECT_TASK_UPDATE)
    public ResponseEntity<Void> editTask(@ProjectId @PathVariable Long projectId, @PathVariable Long taskId,
                                         @Valid @RequestBody TaskEditRequest request) {
        log.debug("TaskControllerV1#editTask called.");
        log.debug("일정 변경을 위한 projectId={}", projectId);
        log.debug("일정 변경을 위한 taskId={}", taskId);
        log.debug("일정 변경을 위한 TaskEditRequest={}", request);

        taskService.editTask(taskId, request.toCommand());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{taskId}")
    @RequirePermission(PermissionType.PROJECT_TASK_DELETE)
    public ResponseEntity<Void> deleteTask(@ProjectId @PathVariable Long projectId, @PathVariable Long taskId) {
        log.debug("TaskControllerV1#deleteTask called.");
        log.debug("일정 삭제를 위한 projectId={}", projectId);
        log.debug("일정 삭제를 위한 taskId={}", taskId);

        taskService.deleteTask(taskId);

        return ResponseEntity.noContent().build();
    }
}
