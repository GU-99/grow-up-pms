package com.growup.pms.status.controller;


import com.growup.pms.status.controller.dto.request.StatusCreateRequest;
import com.growup.pms.status.service.StatusService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/status")
// TODO: 향후 권한 인가가 구현되면 각 요청이 올바른 사용자로부터 온 요청인지 검사해야 함
public class StatusControllerV1 {

    private final StatusService statusService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createStatus(@Valid @RequestBody StatusCreateRequest request) {
        log.debug("StatusControllerV1#createStatus called.");
        log.debug("StatusCreateRequest={}", request);

        Long saveId = statusService.createStatus(request.toServiceDto());
        log.debug("saveId={}", saveId);

        return ResponseEntity.created(URI.create("/api/v1/status/" + saveId)).build();
    }

}
