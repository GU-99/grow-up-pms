package com.growup.pms.task.controller;

import com.growup.pms.common.aop.annotation.ProjectId;
import com.growup.pms.common.aop.annotation.RequireProjectPermission;
import com.growup.pms.common.aop.annotation.RequireTeamPermission;
import com.growup.pms.role.domain.ProjectPermission;
import com.growup.pms.role.domain.TeamPermission;
import com.growup.pms.task.controller.dto.request.TaskUserCreateRequest;
import com.growup.pms.task.controller.dto.response.TaskUserResponse;
import com.growup.pms.task.service.TaskUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * Creates a new task assignee for a specific task within a project.
     *
     * @param projectId The ID of the project containing the task. Must be a positive number.
     * @param taskId The ID of the task to which the user will be assigned. Must be a positive number.
     * @param request The request containing the user ID to be assigned to the task. Must be valid.
     * @return A ResponseEntity with an empty body and 200 OK status upon successful task user creation.
     */
    @PostMapping
    @RequireProjectPermission(ProjectPermission.UPDATE_TASK)
    public ResponseEntity<Void> createTaskUser(
            @Positive @PathVariable @ProjectId Long projectId,
            @Positive @PathVariable Long taskId,
            @Valid @RequestBody TaskUserCreateRequest request
    ) {
        log.debug("TaskUserControllerV1#createTaskUser called.");

        taskUserService.createTaskUser(taskId, request.userId());
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves a list of assignees for a specific task within a project.
     *
     * @param projectId The unique identifier of the project containing the task. Must be a positive number.
     * @param taskId The unique identifier of the task for which assignees are being retrieved. Must be a positive number.
     * @return A ResponseEntity containing a list of TaskUserResponse objects representing the task's assignees.
     * @throws ConstraintViolationException If the project or task ID is not a positive number.
     */
    @GetMapping
    @RequireTeamPermission(TeamPermission.READ_PROJECT)
    public ResponseEntity<List<TaskUserResponse>> getAssignees(
            @Positive @PathVariable @ProjectId Long projectId,
            @Positive @PathVariable Long taskId
    ) {
        log.debug("TaskUserControllerV1#getAssignees called.");

        List<TaskUserResponse> responses = taskUserService.getAssignees(projectId, taskId);
        return ResponseEntity.ok().body(responses);
    }

    /**
     * Deletes a specific assignee from a task within a project.
     *
     * @param projectId The ID of the project containing the task, must be a positive number
     * @param taskId The ID of the task from which the assignee will be removed, must be a positive number
     * @param assigneeId The ID of the assignee to be deleted, must be a positive number
     * @return A ResponseEntity with no content (204 No Content) upon successful deletion
     * @throws ConstraintViolationException if any of the input IDs are not positive
     */
    @DeleteMapping("/{assigneeId}")
    @RequireProjectPermission(ProjectPermission.UPDATE_TASK)
    public ResponseEntity<Void> deleteTaskUser(
            @Positive @PathVariable @ProjectId Long projectId,
            @Positive @PathVariable Long taskId,
            @Positive @PathVariable Long assigneeId
    ) {
        log.debug("TaskUserControllerV1#deleteTaskUser called.");
        taskUserService.deleteTaskUser(taskId, assigneeId);
        return ResponseEntity.noContent().build();
    }
}
