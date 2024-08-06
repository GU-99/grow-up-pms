package com.growup.pms.status.controller;


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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/status")
// TODO: 향후 권한 인가가 구현되면 각 요청이 올바른 사용자로부터 온 요청인지 검사해야 함
public class StatusControllerV1 {

    private final StatusService statusService;

    @PostMapping
    public ResponseEntity<StatusResponse> createStatus(@Valid @RequestBody StatusCreateRequest request) {
        log.debug("StatusControllerV1#createStatus called.");
        log.debug("StatusCreateRequest={}", request);

        StatusResponse response = statusService.createStatus(request.toServiceDto());
        log.debug("response={}", response);

        return ResponseEntity.created(URI.create("/api/v1/status/" + response.getStatusId()))
                .body(response);
    }


    @GetMapping
    public ResponseEntity<PageResponse<List<StatusResponse>>> getStatuses(@RequestParam Long projectId) {
        log.debug("StatusControllerV1#getStatuses called.");
        log.debug("projectId={}", projectId);

        PageResponse<List<StatusResponse>> response = statusService.getStatuses(projectId);
        log.debug("response={}", response);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{statusId}")
    public ResponseEntity<Void> editStatus(@PathVariable Long statusId, @Valid @RequestBody StatusEditRequest request) {
        log.debug("StatusControllerV1#editStatus called.");
        log.debug("statusId={}", statusId);
        log.debug("request={}", request);

        statusService.editStatus(request.toServiceDto(statusId));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{statusId}/order")
    public ResponseEntity<Void> editStatusOrder(@PathVariable Long statusId, @RequestParam Short sortOrder) {
        log.debug("StatusControllerV1#editStatusOrder called.");
        log.debug("statusId={}", statusId);
        log.debug("sortOrder={}", sortOrder);

        statusService.editStatusOrder(statusId, sortOrder);

        return ResponseEntity.noContent().build();
    }
}
