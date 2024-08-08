package com.growup.pms.status.controller;


import com.growup.pms.common.aop.annotation.RequirePermission;
import com.growup.pms.role.domain.PermissionType;
import com.growup.pms.status.controller.dto.request.StatusCreateRequest;
import com.growup.pms.status.controller.dto.request.StatusEditRequest;
import com.growup.pms.status.controller.dto.response.PageResponse;
import com.growup.pms.status.controller.dto.response.StatusResponse;
import com.growup.pms.status.service.StatusService;
import jakarta.validation.Valid;
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
    @RequirePermission(PermissionType.PROJECT_STATUS_WRITE)
    public ResponseEntity<StatusResponse> createStatus(@PathVariable Long projectId,
                                                       @Valid @RequestBody StatusCreateRequest request) {
        log.debug("StatusControllerV1#createStatus called.");
        log.debug("projectId={}", projectId);
        log.debug("StatusCreateRequest={}", request);

        StatusResponse response = statusService.createStatus(request.toCommand(projectId));
        log.debug("response={}", response);

        return ResponseEntity.created(URI.create("/api/v1/project/" + projectId + "/status/" + response.statusId()))
                .body(response);
    }


    @GetMapping
    public ResponseEntity<PageResponse<List<StatusResponse>>> getStatuses(@PathVariable Long projectId) {
        log.debug("StatusControllerV1#getStatuses called.");
        log.debug("projectId={}", projectId);

        PageResponse<List<StatusResponse>> response = statusService.getStatuses(projectId);
        log.debug("response={}", response);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{statusId}")
    @RequirePermission(PermissionType.PROJECT_STATUS_WRITE)
    public ResponseEntity<Void> editStatus(@PathVariable Long projectId, @PathVariable Long statusId,
                                           @Valid @RequestBody StatusEditRequest request) {
        log.debug("StatusControllerV1#editStatus called.");
        log.debug("statusId={}", statusId);
        log.debug("request={}", request);

        statusService.editStatus(request.toCommand(statusId));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{statusId}")
    @RequirePermission(PermissionType.PROJECT_STATUS_DELETE)
    public ResponseEntity<Void> deleteStatus(@PathVariable Long projectId, @PathVariable Long statusId) {
        log.debug("StatusControllerV1#deleteStatus called.");
        log.debug("statusId={}", statusId);

        statusService.deleteStatus(statusId);

        return ResponseEntity.noContent().build();
    }
}
