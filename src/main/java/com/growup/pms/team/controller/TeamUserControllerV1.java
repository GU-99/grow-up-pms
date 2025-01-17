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

    /**
     * Retrieves all users within a specified team.
     *
     * @param teamId The unique identifier of the team, must be a positive long value
     * @return A ResponseEntity containing a list of team users with HTTP 200 OK status
     * @throws ConstraintViolationException if the teamId is not a positive value
     */
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

    /**
     * Removes a specified member from a team.
     *
     * @param teamId The unique identifier of the team from which the member will be kicked. Must be a positive long value.
     * @param targetMemberId The unique identifier of the member to be removed from the team. Must be a positive long value.
     * @return A ResponseEntity with no content, indicating a successful member removal
     * @throws NotFoundException If the team or target member does not exist
     * @throws UnauthorizedException If the current user lacks permission to kick members
     */
    @DeleteMapping("/{targetMemberId}")
    @RequireTeamPermission(TeamPermission.KICK_MEMBER)
    public ResponseEntity<Void> kickMember(
            @Positive @PathVariable @TeamId Long teamId,
            @Positive @PathVariable Long targetMemberId
    ) {
        teamUserService.kickMember(teamId, targetMemberId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Changes the role of a team member.
     *
     * @param teamId The ID of the team where the role change is being performed
     * @param targetMemberId The ID of the member whose role is being updated
     * @param request Contains the new role name to be assigned to the member
     * @return A ResponseEntity with an OK status upon successful role update
     * @throws NotFoundException if the team or member does not exist
     * @throws UnauthorizedException if the current user lacks permission to update member roles
     */
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
