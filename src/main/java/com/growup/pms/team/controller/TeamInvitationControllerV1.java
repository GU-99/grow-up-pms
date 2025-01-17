package com.growup.pms.team.controller;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.common.aop.annotation.CurrentUser;
import com.growup.pms.common.aop.annotation.RequireTeamPermission;
import com.growup.pms.common.aop.annotation.TeamId;
import com.growup.pms.role.domain.TeamPermission;
import com.growup.pms.team.controller.dto.request.TeamInvitationCreateRequest;
import com.growup.pms.team.service.TeamInvitationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team/{teamId}/invitation")
public class TeamInvitationControllerV1 {

    private final TeamInvitationService teamInvitationService;

    /**
     * Sends an invitation to join a team.
     *
     * @param teamId The unique identifier of the team to which a member is being invited. Must be a positive number.
     * @param request The details of the team invitation to be created. Must be a valid {@link TeamInvitationCreateRequest}.
     * @return A {@link ResponseEntity} with HTTP 200 OK status upon successful invitation creation.
     * @throws ConstraintViolationException if the team ID is not positive or the request is invalid.
     */
    @PostMapping
    @RequireTeamPermission(TeamPermission.INVITE_MEMBER)
    public ResponseEntity<Void> invite(
            @Positive @PathVariable @TeamId Long teamId,
            @Valid @RequestBody TeamInvitationCreateRequest request
    ) {
        teamInvitationService.sendInvitation(teamId, request.toCommand());
        return ResponseEntity.ok().build();
    }

    /**
     * Accepts a team invitation for the current user.
     *
     * @param user The currently authenticated user making the invitation acceptance request
     * @param teamId The unique identifier of the team for which the invitation is being accepted
     * @return A ResponseEntity with an HTTP 200 OK status upon successful invitation acceptance
     */
    @PostMapping("/accept")
    public ResponseEntity<Void> accept(
            @CurrentUser SecurityUser user,
            @Positive @PathVariable Long teamId
    ) {
        teamInvitationService.acceptInvitation(teamId, user.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * Declines a team invitation for the current user.
     *
     * @param user    The current authenticated user making the invitation decline request
     * @param teamId  The unique identifier of the team for which the invitation is being declined
     * @return        A ResponseEntity with HTTP 200 OK status upon successful invitation decline
     */
    @PostMapping("/decline")
    public ResponseEntity<Void> decline(
            @CurrentUser SecurityUser user,
            @Positive @PathVariable Long teamId
    ) {
        teamInvitationService.declineInvitation(teamId, user.getId());
        return ResponseEntity.ok().build();
    }
}

