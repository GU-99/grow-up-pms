package com.growup.pms.team.controller;

import com.growup.pms.common.aop.annotation.RequirePermission;
import com.growup.pms.common.aop.annotation.TeamId;
import com.growup.pms.role.domain.PermissionType;
import com.growup.pms.team.controller.dto.request.RoleUpdateRequest;
import com.growup.pms.team.service.TeamUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team/{teamId}/user")
public class TeamUserControllerV1 {
    private final TeamUserService teamUserService;

    @DeleteMapping("/{targetMemberId}")
    @RequirePermission(PermissionType.TEAM_KICK_MEMBER)
    public ResponseEntity<Void> kickMember(
            @Positive @PathVariable @TeamId Long teamId,
            @Positive @PathVariable Long targetMemberId
    ) {
        teamUserService.kickMember(teamId, targetMemberId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{targetMemberId}/role")
    @RequirePermission(PermissionType.TEAM_MEMBER_ROLE_UPDATE)
    public ResponseEntity<Void> changeRole(
            @Positive @PathVariable @TeamId Long teamId,
            @Positive @PathVariable Long targetMemberId,
            @Valid @RequestBody RoleUpdateRequest request
    ) {
        teamUserService.changeRole(teamId, targetMemberId, request.role());
        return ResponseEntity.ok().build();
    }
}
