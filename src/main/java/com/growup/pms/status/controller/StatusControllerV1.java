package com.growup.pms.status.controller;


import com.growup.pms.status.controller.dto.request.CreateStatusRequest;
import com.growup.pms.status.service.StatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/status")
public class StatusControllerV1 {

    private final StatusService statusService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> createStatus(@Valid @RequestBody CreateStatusRequest request) {
        log.debug("StatusControllerV1#createStatus called.");
        log.debug("StatusCreateRequest={}", request);

        Long saveId = statusService.createStatus(request.toServiceDto());
        log.debug("saveId={}", saveId);

        return ApiResponse.created(saveId);
    }

}
