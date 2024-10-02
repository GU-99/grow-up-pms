package com.growup.pms.project.controller;

import com.growup.pms.common.aop.annotation.ProjectId;
import com.growup.pms.common.aop.annotation.RequirePermission;
import com.growup.pms.project.controller.dto.request.ProjectRoleEditRequest;
import com.growup.pms.project.controller.dto.request.ProjectUserCreateRequest;
import com.growup.pms.project.service.ProjectUserService;
import com.growup.pms.role.domain.PermissionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project/{projectId}/user")
public class ProjectUserControllerV1 {

    private final ProjectUserService projectUserService;

    @PostMapping
    @RequirePermission(PermissionType.PROJECT_INVITE_MEMBER)
    public ResponseEntity<Void> createProjectUser(@Positive @ProjectId @PathVariable Long projectId,
                                                  @Valid @RequestBody ProjectUserCreateRequest request) {
        log.debug("ProjectUserControllerV1#createProjectUser called.");
        log.debug("프로젝트원 초대를 위한 projectId: {}", projectId);
        log.debug("프로젝트원 초대를 위한 ProjectInvitationRequest: {}", request);

        projectUserService.createProjectUser(projectId, request.toCommand());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    @RequirePermission(PermissionType.PROJECT_KICK_MEMBER)
    public ResponseEntity<Void> kickProjectUser(@Positive @ProjectId @PathVariable Long projectId,
                                                @Positive @PathVariable Long userId) {
        log.debug("ProjectUserControllerV1#kickProjectUser called.");
        log.debug("프로젝트원 탈퇴를 위한 projectId: {}", projectId);
        log.debug("프로젝트원 탈퇴를 위한 userId: {}", userId);

        projectUserService.kickProjectUser(projectId, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{targetUserId}/role")
    @RequirePermission(PermissionType.PROJECT_MEMBER_ROLE_UPDATE)
    public ResponseEntity<Void> changeRole(
            @Positive @PathVariable @ProjectId Long projectId,
            @Positive @PathVariable Long targetUserId,
            @Valid @RequestBody ProjectRoleEditRequest request
    ) {
        projectUserService.changeRole(projectId, targetUserId, request.roleName());
        return ResponseEntity.ok().build();
    }
}
