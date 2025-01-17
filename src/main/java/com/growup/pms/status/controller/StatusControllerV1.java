package com.growup.pms.status.controller;


import com.growup.pms.common.aop.annotation.ProjectId;
import com.growup.pms.common.aop.annotation.RequireProjectPermission;
import com.growup.pms.common.aop.annotation.RequireTeamPermission;
import com.growup.pms.role.domain.ProjectPermission;
import com.growup.pms.role.domain.TeamPermission;
import com.growup.pms.status.controller.dto.request.StatusCreateRequest;
import com.growup.pms.status.controller.dto.request.StatusEditRequest;
import com.growup.pms.status.controller.dto.request.StatusOrderListEditRequest;
import com.growup.pms.status.controller.dto.response.StatusResponse;
import com.growup.pms.status.service.StatusService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project/{projectId}/status")
public class StatusControllerV1 {

    private final StatusService statusService;

    /**
     * Creates a new status for a specific project.
     *
     * @param projectId The unique identifier of the project where the status will be created.
     * @param request The request containing details for creating a new status.
     * @return A ResponseEntity containing the created status details with a 201 Created status.
     *
     * @throws ConstraintViolationException If the input validation fails.
     *
     * @see StatusCreateRequest
     * @see StatusResponse
     */
    @PostMapping
    @RequireProjectPermission(ProjectPermission.CREATE_STATUS)
    public ResponseEntity<StatusResponse> createStatus(
            @Positive @PathVariable @ProjectId Long projectId,
            @Valid @RequestBody StatusCreateRequest request
    ) {
        log.debug("StatusControllerV1#createStatus called.");
        log.debug("projectId={}", projectId);
        log.debug("StatusCreateRequest={}", request);

        StatusResponse response = statusService.createStatus(request.toCommand(projectId));
        log.debug("response={}", response);

        return ResponseEntity.created(URI.create("/api/v1/project/" + projectId + "/status/" + response.statusId()))
                .body(response);
    }


    /**
     * Retrieves all statuses for a specific project.
     *
     * @param projectId The unique identifier of the project to fetch statuses for. Must be a positive number.
     * @return A ResponseEntity containing a list of StatusResponse objects representing the project's statuses.
     * @throws ConstraintViolationException if the projectId is not a positive number
     */
    @GetMapping
    @RequireTeamPermission(TeamPermission.READ_PROJECT)
    public ResponseEntity<List<StatusResponse>> getStatuses(@Positive @PathVariable @ProjectId Long projectId) {
        log.debug("StatusControllerV1#getStatuses called.");
        log.debug("projectId={}", projectId);

        List<StatusResponse> response = statusService.getStatuses(projectId);
        log.debug("response={}", response);

        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing status within a project.
     *
     * @param projectId The ID of the project containing the status to be edited
     * @param statusId The unique identifier of the status to be modified
     * @param request The request containing details for updating the status
     * @return A ResponseEntity with no content, indicating a successful status update
     *
     * @throws ConstraintViolationException if the input parameters fail validation
     */
    @PatchMapping("/{statusId}")
    @RequireProjectPermission(ProjectPermission.UPDATE_STATUS)
    public ResponseEntity<Void> editStatus(
            @Positive @PathVariable @ProjectId Long projectId,
            @Positive @PathVariable Long statusId,
            @Valid @RequestBody StatusEditRequest request
    ) {
        log.debug("StatusControllerV1#editStatus called.");
        log.debug("projectId={}", projectId);
        log.debug("statusId={}", statusId);
        log.debug("request={}", request);

        statusService.editStatus(request.toCommand(statusId));

        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the order of statuses for a specific project.
     *
     * @param projectId The unique identifier of the project whose status order is being modified
     * @param request A valid request containing the new order of statuses
     * @return A 204 No Content response indicating successful status order update
     *
     * @throws ConstraintViolationException if the request validation fails
     */
    @PatchMapping("/order")
    @RequireProjectPermission(ProjectPermission.UPDATE_STATUS)
    public ResponseEntity<Void> editStatusOrder(
            @Positive @PathVariable @ProjectId Long projectId,
            @Valid @RequestBody StatusOrderListEditRequest request
    ) {
        log.debug("StatusControllerV1#editStatusOrder called.");
        log.debug("projectId={}", projectId);
        log.debug("request={}", request);

        statusService.editStatusOrder(request.toCommands());

        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a specific status within a project.
     *
     * @param projectId The unique identifier of the project containing the status to be deleted
     * @param statusId The unique identifier of the status to be deleted
     * @return A ResponseEntity with no content (204 No Content) indicating successful deletion
     * @throws ConstraintViolationException If the project or status ID is invalid or negative
     */
    @DeleteMapping("/{statusId}")
    @RequireProjectPermission(ProjectPermission.DELETE_STATUS)
    public ResponseEntity<Void> deleteStatus(
            @Positive @PathVariable @ProjectId Long projectId,
            @Positive @PathVariable Long statusId
    ) {
        log.debug("StatusControllerV1#deleteStatus called.");
        log.debug("projectId={}", projectId);
        log.debug("statusId={}", statusId);

        statusService.deleteStatus(projectId, statusId);

        return ResponseEntity.noContent().build();
    }
}
