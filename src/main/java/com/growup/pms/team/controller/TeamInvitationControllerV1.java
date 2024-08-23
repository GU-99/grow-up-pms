package com.growup.pms.team.controller;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.common.aop.annotation.RequirePermission;
import com.growup.pms.common.aop.annotation.TeamId;
import com.growup.pms.role.domain.PermissionType;
import com.growup.pms.team.controller.dto.request.TeamInvitationCreateRequest;
import com.growup.pms.team.service.TeamInvitationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/team/{teamId}/invitation")
@RequiredArgsConstructor
public class TeamInvitationControllerV1 {
    private final TeamInvitationService teamInvitationService;

    @PostMapping
    @RequirePermission(PermissionType.TEAM_INVITE_MEMBER)
    public ResponseEntity<Void> invite(
            @Positive @PathVariable @TeamId Long teamId,
            @Valid @RequestBody TeamInvitationCreateRequest request
    ) {
        teamInvitationService.sendInvitation(teamId, request.toCommand());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> accept(
            @AuthenticationPrincipal SecurityUser user,
            @Positive @PathVariable Long teamId
    ) {
        teamInvitationService.acceptInvitation(teamId, user.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decline")
    public ResponseEntity<Void> decline(
            @AuthenticationPrincipal SecurityUser user,
            @Positive @PathVariable Long teamId
    ) {
        teamInvitationService.declineInvitation(teamId, user.getId());
        return ResponseEntity.ok().build();
    }
}
