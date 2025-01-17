package com.growup.pms.project.controller;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.common.aop.annotation.CurrentUser;
import com.growup.pms.common.aop.annotation.ProjectId;
import com.growup.pms.common.aop.annotation.RequireProjectPermission;
import com.growup.pms.common.aop.annotation.RequireTeamPermission;
import com.growup.pms.common.aop.annotation.TeamId;
import com.growup.pms.project.controller.dto.request.ProjectCreateRequest;
import com.growup.pms.project.controller.dto.request.ProjectEditRequest;
import com.growup.pms.project.controller.dto.response.ProjectResponse;
import com.growup.pms.project.service.ProjectService;
import com.growup.pms.role.domain.ProjectPermission;
import com.growup.pms.role.domain.TeamPermission;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team/{teamId}/project")
public class ProjectControllerV1 {

    private final ProjectService projectService;

    /**
     * Creates a new project for a specified team.
     *
     * @param teamId The unique identifier of the team where the project will be created
     * @param user The current authenticated user creating the project
     * @param request The project creation request containing project details
     * @return ResponseEntity with a 201 Created status and the URI of the newly created project
     *
     * @throws PermissionDeniedException If the user lacks permission to create a project in the team
     * @throws InvalidRequestException If the project creation request is invalid
     *
     * @see ProjectService#createProject(Long, Long, ProjectCreateCommand)
     */
    @PostMapping
    @RequireTeamPermission(TeamPermission.CREATE_PROJECT)
    public ResponseEntity<Void> createProject(
            @Positive @PathVariable @TeamId Long teamId,
            @CurrentUser SecurityUser user,
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

    /**
     * Retrieves a list of projects for a specific team.
     *
     * @param teamId The unique identifier of the team whose projects are to be retrieved. Must be a positive number.
     * @return A ResponseEntity containing a list of ProjectResponse objects representing the team's projects
     */
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects(@Positive @PathVariable Long teamId) {
        log.debug("ProjectControllerV1#getProjects called.");
        log.debug("프로젝트 목록을 조회하려는 팀 ID={}", teamId);

        List<ProjectResponse> responses = projectService.getProjects(teamId);

        return ResponseEntity.ok(responses);
    }

    /**
     * Updates an existing project with the provided details.
     *
     * @param teamId The ID of the team to which the project belongs
     * @param projectId The unique identifier of the project to be edited
     * @param request The project edit request containing updated project information
     * @return A ResponseEntity with no content, indicating a successful update
     * @throws NotFoundException if the project does not exist
     * @throws UnauthorizedException if the user lacks permission to update the project
     */
    @PatchMapping("/{projectId}")
    @RequireProjectPermission(ProjectPermission.UPDATE_PROJECT)
    public ResponseEntity<Void> editProject(@PathVariable Long teamId, @PathVariable @ProjectId Long projectId,
                                            @Valid @RequestBody ProjectEditRequest request) {
        log.debug("ProjectControllerV1#editProject called.");

        projectService.editProject(projectId, request.toCommand());

        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a specific project within a team.
     *
     * @param teamId     The ID of the team containing the project to be deleted
     * @param projectId  The unique identifier of the project to delete
     * @return           A ResponseEntity with no content (204 No Content) indicating successful deletion
     * @throws ProjectNotFoundException if the specified project does not exist
     * @throws UnauthorizedAccessException if the user lacks permission to delete the project
     */
    @DeleteMapping("/{projectId}")
    @RequireProjectPermission(ProjectPermission.DELETE_PROJECT)
    public ResponseEntity<Void> deleteProject(@PathVariable Long teamId, @PathVariable @ProjectId Long projectId) {
        log.debug("ProjectControllerV1#deleteProject called.");

        projectService.deleteProject(projectId);

        return ResponseEntity.noContent().build();
    }

    /**
     * Allows a user to leave a specific project within a team.
     *
     * @param teamId The ID of the team containing the project
     * @param projectId The ID of the project to leave
     * @param user The currently authenticated user attempting to leave the project
     * @return A 204 No Content response indicating successful project departure
     */
    @DeleteMapping("/{projectId}/leave")
    public ResponseEntity<Void> leaveProject(
            @Positive @PathVariable Long teamId,
            @Positive @PathVariable Long projectId,
            @CurrentUser SecurityUser user) {
        projectService.leaveProject(teamId, projectId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
