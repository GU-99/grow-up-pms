package com.growup.pms.project.controller;

import com.growup.pms.common.aop.annotation.ProjectId;
import com.growup.pms.common.aop.annotation.RequireProjectPermission;
import com.growup.pms.project.controller.dto.request.ProjectRoleEditRequest;
import com.growup.pms.project.controller.dto.request.ProjectUserCreateRequest;
import com.growup.pms.project.controller.dto.response.ProjectUserResponse;
import com.growup.pms.project.controller.dto.response.ProjectUserSearchResponse;
import com.growup.pms.project.service.ProjectUserService;
import com.growup.pms.role.domain.ProjectPermission;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project/{projectId}/user")
public class ProjectUserControllerV1 {

    private final ProjectUserService projectUserService;

    /**
     * Creates a new user for a specific project.
     *
     * @param projectId The unique identifier of the project to which the user will be invited. Must be a positive number.
     * @param request The details of the user to be invited to the project. Must be a valid {@link ProjectUserCreateRequest}.
     * @return A {@link ResponseEntity} with an HTTP 200 OK status if the user is successfully created.
     * @throws ValidationException if the project ID or user request is invalid
     * @throws PermissionDeniedException if the user lacks permission to invite members
     */
    @PostMapping
    @RequireProjectPermission(ProjectPermission.INVITE_MEMBER)
    public ResponseEntity<Void> createProjectUser(@Positive @PathVariable @ProjectId Long projectId,
                                                  @Valid @RequestBody ProjectUserCreateRequest request) {
        log.debug("ProjectUserControllerV1#createProjectUser called.");
        log.debug("프로젝트원 초대를 위한 projectId: {}", projectId);
        log.debug("프로젝트원 초대를 위한 ProjectInvitationRequest: {}", request);

        projectUserService.createProjectUser(projectId, request.toCommand());
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves all users associated with a specific project.
     *
     * @param projectId The unique identifier of the project to fetch users from. Must be a positive number.
     * @return A ResponseEntity containing a list of ProjectUserResponse objects representing the project's users.
     * @throws IllegalArgumentException If the provided projectId is not positive.
     */
    @GetMapping
    public ResponseEntity<List<ProjectUserResponse>> getProjectUsers(
            @Positive @PathVariable Long projectId) {
        log.debug("ProjectUserControllerV1#getProjectUsers called.");
        log.debug("프로젝트원을 조회할 projectId: {}", projectId);
        List<ProjectUserResponse> responses = projectUserService.getProjectUsers(projectId);

        return ResponseEntity.ok(responses);
    }

    /**
     * Searches for project users by a nickname prefix within a specific project.
     *
     * @param projectId The unique identifier of the project to search users in. Must be a positive number.
     * @param prefix Optional nickname prefix to filter users. Defaults to an empty string if not provided.
     * @return A list of project user search results matching the given prefix, wrapped in a ResponseEntity.
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProjectUserSearchResponse>> searchProjectUsersByPrefix(
            @Positive @PathVariable Long projectId,
            @RequestParam(required = false, defaultValue = "", name = "nickname") String prefix
    ) {
        log.debug("ProjectUserControllerV1#searchProjectUsers called.");
        log.debug("검색어: {}", prefix);

        List<ProjectUserSearchResponse> responses = projectUserService.searchProjectUsersByPrefix(projectId,
                prefix);
        return ResponseEntity.ok(responses);
    }

    /**
     * Updates the role of a specific user within a project.
     *
     * @param projectId The unique identifier of the project where the role change occurs
     * @param targetUserId The unique identifier of the user whose role is being modified
     * @param request Contains the new role name to be assigned to the user
     * @return A ResponseEntity with an HTTP 200 OK status upon successful role modification
     * @throws NotFoundException If the project or user cannot be found
     * @throws UnauthorizedException If the current user lacks permission to update member roles
     */
    @PatchMapping("/{targetUserId}/role")
    @RequireProjectPermission(ProjectPermission.UPDATE_MEMBER_ROLE)
    public ResponseEntity<Void> changeRole(
            @Positive @PathVariable @ProjectId Long projectId,
            @Positive @PathVariable Long targetUserId,
            @Valid @RequestBody ProjectRoleEditRequest request
    ) {
        projectUserService.changeRole(projectId, targetUserId, request.roleName());
        return ResponseEntity.ok().build();
    }

    /**
     * Removes a user from a specific project.
     *
     * @param projectId The unique identifier of the project from which the user will be removed. Must be a positive number.
     * @param userId The unique identifier of the user to be removed from the project. Must be a positive number.
     * @return A ResponseEntity with no content (204 No Content) indicating successful user removal
     * @throws IllegalArgumentException if the project or user ID is invalid
     * @throws AccessDeniedException if the user lacks permission to kick a project member
     */
    @DeleteMapping("/{userId}")
    @RequireProjectPermission(ProjectPermission.KICK_MEMBER)
    public ResponseEntity<Void> kickProjectUser(@Positive @PathVariable @ProjectId Long projectId,
                                                @Positive @PathVariable Long userId) {
        log.debug("ProjectUserControllerV1#kickProjectUser called.");
        log.debug("프로젝트원 탈퇴를 위한 projectId: {}", projectId);
        log.debug("프로젝트원 탈퇴를 위한 userId: {}", userId);

        projectUserService.kickProjectUser(projectId, userId);
        return ResponseEntity.noContent().build();
    }
}
