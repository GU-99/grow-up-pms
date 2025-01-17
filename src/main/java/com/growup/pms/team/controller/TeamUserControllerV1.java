package com.growup.pms.team.controller;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.common.aop.annotation.CurrentUser;
import com.growup.pms.common.aop.annotation.RequireTeamPermission;
import com.growup.pms.common.aop.annotation.TeamId;
import com.growup.pms.role.domain.TeamPermission;
import com.growup.pms.team.controller.dto.request.RoleUpdateRequest;
import com.growup.pms.team.controller.dto.response.TeamUserResponse;
import com.growup.pms.team.controller.dto.response.TeamUserSearchResponse;
import com.growup.pms.team.service.TeamUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public ResponseEntity<List<TeamUserResponse>> getAllTeamUsers(@Positive @PathVariable Long teamId) {
        return ResponseEntity.ok(teamUserService.getAllTeamUsers(teamId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeamUserSearchResponse>> searchTeamUsers(
            @CurrentUser SecurityUser user,
            @Valid @Positive @PathVariable Long teamId,
            @Valid @NotEmpty String nickname
    ) {
        return ResponseEntity.ok(teamUserService.getTeamUsersByNicknameStartingWith(user.getId(), teamId, nickname));
    }

    @DeleteMapping("/{targetMemberId}")
    @RequireTeamPermission(TeamPermission.KICK_MEMBER)
    public ResponseEntity<Void> kickMember(
            @Positive @PathVariable @TeamId Long teamId,
            @Positive @PathVariable Long targetMemberId
    ) {
        teamUserService.kickMember(teamId, targetMemberId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{targetMemberId}/role")
    @RequireTeamPermission(TeamPermission.UPDATE_MEMBER_ROLE)
    public ResponseEntity<Void> changeRole(
            @Positive @PathVariable @TeamId Long teamId,
            @Positive @PathVariable Long targetMemberId,
            @Valid @RequestBody RoleUpdateRequest request
    ) {
        teamUserService.changeRole(teamId, targetMemberId, request.roleName());
        return ResponseEntity.ok().build();
    }
}
