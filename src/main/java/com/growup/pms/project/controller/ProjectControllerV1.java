package com.growup.pms.project.controller;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.common.aop.annotation.RequirePermission;
import com.growup.pms.common.aop.annotation.TeamId;
import com.growup.pms.project.controller.dto.request.ProjectCreateRequest;
import com.growup.pms.project.service.ProjectService;
import com.growup.pms.role.domain.PermissionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team/{teamId}/project")
public class ProjectControllerV1 {

    private final ProjectService projectService;

    @PostMapping
    @RequirePermission(PermissionType.TEAM_PROJECT_CREATE)
    public ResponseEntity<Void> createProject(
            @Positive @TeamId @PathVariable Long teamId,
            @AuthenticationPrincipal SecurityUser user,
            @Valid @RequestBody ProjectCreateRequest request) {
        log.debug("ProjectControllerV1#createProject called.");
        log.debug("프로젝트 생성을 위한 팀 ID={}", teamId);
        log.debug("프로젝트를 생성하는 회원={}", user);
        log.debug("프로젝트 생성을 위한 ProjectCreateRequest={}", request);

        Long savedId = projectService.createProject(teamId, user.getId(), request.toCommand());

        String uri = UriComponentsBuilder.fromPath("/api/v1/team/{teamId}/project/{projectId}")
                .buildAndExpand(teamId, savedId)
                .toUriString();

        return ResponseEntity.created(URI.create(uri)).build();
    }
}
