package com.growup.pms.team.controller;

import static com.growup.pms.common.constant.RegexConstants.TEAM_NAME_PATTERN;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.common.aop.annotation.CurrentUser;
import com.growup.pms.common.aop.annotation.RequireTeamPermission;
import com.growup.pms.common.aop.annotation.TeamId;
import com.growup.pms.role.domain.TeamPermission;
import com.growup.pms.team.controller.dto.request.TeamCreateRequest;
import com.growup.pms.team.controller.dto.request.TeamHeadUpdateRequest;
import com.growup.pms.team.controller.dto.request.TeamUpdateRequest;
import com.growup.pms.team.controller.dto.response.TeamNameCheckResponse;
import com.growup.pms.team.controller.dto.response.TeamResponse;
import com.growup.pms.team.service.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team")
public class TeamControllerV1 {

    private final TeamService teamService;

    /**
     * Creates a new team for the authenticated user.
     *
     * @param user The currently authenticated user, automatically injected by the @CurrentUser annotation
     * @param request The team creation request containing team details, validated by @Valid annotation
     * @return ResponseEntity with a 201 Created status and the URI of the newly created team
     */
    @PostMapping
    public ResponseEntity<Void> createTeam(
            @CurrentUser SecurityUser user,
            @Valid @RequestBody TeamCreateRequest request
    ) {
        return ResponseEntity.created(URI.create("/api/v1/team/"
                        + teamService.createTeam(user.getId(), request.toCommand())))
                .build();
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeam(@Positive @PathVariable Long teamId) {
        return ResponseEntity.ok()
                .body(teamService.getTeam(teamId));
    }

    /**
     * Updates an existing team with the provided details.
     *
     * @param teamId The unique identifier of the team to be updated. Must be a positive number.
     * @param request The request containing the team update details, validated for correctness.
     * @return A ResponseEntity with no content, indicating a successful team update.
     *
     * @throws ConstraintViolationException If the teamId is not positive or the request is invalid.
     * @throws TeamNotFoundException If no team exists with the given teamId.
     * @throws UnauthorizedAccessException If the user lacks permission to update the team.
     */
    @PatchMapping("/{teamId}")
    @RequireTeamPermission(TeamPermission.UPDATE_TEAM)
    public ResponseEntity<Void> updateTeam(
            @Positive @PathVariable @TeamId Long teamId,
            @Valid @RequestBody TeamUpdateRequest request
    ) {
        teamService.updateTeam(teamId, request.toCommand());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{teamId}/leave")
    public ResponseEntity<Void> leaveTeam(@CurrentUser SecurityUser user, @Positive @PathVariable Long teamId) {
        teamService.leaveTeam(teamId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<TeamNameCheckResponse> isTeamNameAvailable(
            @Valid @NotNull @Pattern(regexp = TEAM_NAME_PATTERN) String teamName
    ) {
        return ResponseEntity.ok().body(teamService.isTeamNameAvailable(teamName));
    }

    @PostMapping("/{teamId}/head/transfer")
    public ResponseEntity<Void> changeTeamHead(
            @CurrentUser SecurityUser user,
            @Positive @PathVariable Long teamId,
            @Valid @RequestBody TeamHeadUpdateRequest request
    ) {
        teamService.changeTeamHead(teamId, user.getId(), request.userId());
        return ResponseEntity.ok().build();
    }
}
