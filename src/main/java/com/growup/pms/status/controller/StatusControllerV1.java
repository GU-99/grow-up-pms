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


    @GetMapping
    @RequireTeamPermission(TeamPermission.READ_PROJECT)
    public ResponseEntity<List<StatusResponse>> getStatuses(@Positive @PathVariable @ProjectId Long projectId) {
        log.debug("StatusControllerV1#getStatuses called.");
        log.debug("projectId={}", projectId);

        List<StatusResponse> response = statusService.getStatuses(projectId);
        log.debug("response={}", response);

        return ResponseEntity.ok(response);
    }

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
