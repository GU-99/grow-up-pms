package com.growup.pms.team.controller;

import com.growup.pms.common.aop.annotation.RequirePermission;
import com.growup.pms.common.aop.annotation.TeamId;
import com.growup.pms.role.domain.PermissionType;
import com.growup.pms.team.service.TeamUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team/{teamId}/user")
public class TeamUserControllerV1 {
    private final TeamUserService teamUserService;

    @DeleteMapping("/{teamId}/user/{targetMemberId}")
    @RequirePermission(PermissionType.TEAM_KICK_MEMBER)
    public ResponseEntity<Void> kickMember(@PathVariable @TeamId Long teamId, @PathVariable Long targetMemberId) {
        teamUserService.kickMember(teamId, targetMemberId);
        return ResponseEntity.noContent().build();
    }
}
