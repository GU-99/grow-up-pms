package com.growup.pms.task.controller;

import com.growup.pms.common.aop.annotation.ProjectId;
import com.growup.pms.common.aop.annotation.RequirePermission;
import com.growup.pms.role.domain.PermissionType;
import com.growup.pms.task.controller.dto.request.TaskUserCreateRequest;
import com.growup.pms.task.service.TaskUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project/{projectId}/task/{taskId}/assignee")
public class TaskUserControllerV1 {

    private final TaskUserService taskUserService;

    @PostMapping
    @RequirePermission(PermissionType.PROJECT_TASK_UPDATE)
    public ResponseEntity<Void> createTaskUser(
            @Positive @ProjectId @PathVariable Long projectId,
            @Positive @PathVariable Long taskId,
            @Valid @RequestBody TaskUserCreateRequest request
    ) {
        log.debug("TaskUserControllerV1#createTaskUser called.");

        taskUserService.createTaskUser(taskId, request.userId());
        return ResponseEntity.ok().build();
    }

}
