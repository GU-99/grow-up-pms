package com.growup.pms.common.aop.aspect;

import com.growup.pms.common.aop.annotation.ProjectId;
import com.growup.pms.common.aop.annotation.RequireProjectPermission;
import com.growup.pms.common.aop.annotation.RequireTeamPermission;
import com.growup.pms.common.aop.annotation.TeamId;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.common.util.AopUtil;
import com.growup.pms.common.util.AuthenticationUtil;
import com.growup.pms.project.service.ProjectService;
import com.growup.pms.role.service.PermissionService;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * AOP 기반의 권한 검사를 수행하는 Aspect 클래스입니다.
 * 팀과 프로젝트에 대한 사용자의 접근 권한을 메서드 실행 전에 검증합니다.
 * 
 * <p>이 Aspect는 다음 애노테이션들과 함께 동작합니다:</p>
 * <ul>
 *   <li>{@link RequireTeamPermission} - 팀 관련 권한 검사</li>
 *   <li>{@link RequireProjectPermission} - 프로젝트 관련 권한 검사</li>
 * </ul>
 *
 * <p><strong>사용 예시:</strong></p>
 * <pre>{@code
 * // 팀 권한 검사
 * @DeleteMapping("/teams/{teamId}")
 * @RequireTeamPermission(TeamPermission.DELETE_TEAM)
 * public ResponseEntity<Void> deleteTeam(@TeamId @PathVariable Long teamId) {
 *     teamService.deleteTeam(teamId);
 *     return ResponseEntity.noContent().build();
 * }
 *
 * // 프로젝트 권한 검사
 * @PutMapping("/projects/{projectId}")
 * @RequireProjectPermission(ProjectPermission.UPDATE_PROJECT)
 * public ResponseEntity<ProjectResponse> updateProject(
 *     @ProjectId @PathVariable Long projectId,
 *     @RequestBody UpdateProjectRequest request
 * ) {
 *     return ResponseEntity.ok(projectService.updateProject(projectId, request));
 * }
 * }</pre>
 *
 * @see RequireTeamPermission
 * @see RequireProjectPermission
 * @see TeamId
 * @see ProjectId
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

    private final ProjectService projectService;
    private final PermissionService permissionService;

    /**
     * Checks team-related permissions before method execution.
     *
     * This advice is invoked for methods annotated with {@link RequireTeamPermission}.
     * It retrieves the team ID from method parameters, either directly via {@link TeamId} annotation
     * or indirectly through a {@link ProjectId} annotation, and validates the current user's permissions.
     *
     * @param joinPoint The join point representing the method being executed
     * @param requireTeamPermission The annotation specifying the required team permissions
     * @throws BusinessException if the current user lacks the necessary team permissions
     */
    @Before(value = "@annotation(requireTeamPermission)", argNames = "joinPoint,requireTeamPermission")
    public void checkTeamPermission(JoinPoint joinPoint, RequireTeamPermission requireTeamPermission) {
        Long teamId = findTeamId(joinPoint);
        permissionService.checkTeamPermission(AuthenticationUtil.getCurrentUser().getId(),
                teamId, Arrays.asList(requireTeamPermission.value()));
    }

    /**
     * Checks project-related permissions before method execution.
     *
     * This advice is invoked before methods annotated with {@link RequireProjectPermission}.
     * It validates whether the current user has the required permissions for a specific project
     * by finding the project ID from method parameters and performing a permission check.
     *
     * @param joinPoint The join point representing the method being executed
     * @param requireProjectPermission Annotation specifying the required project permissions
     * @throws BusinessException If the user lacks the necessary project permissions
     * @throws IllegalStateException If no parameter with {@link ProjectId} annotation of type Long is found
     */
    @Before(value = "@annotation(requireProjectPermission)", argNames = "joinPoint,requireProjectPermission")
    public void checkProjectPermission(JoinPoint joinPoint, RequireProjectPermission requireProjectPermission) {
        Long projectId = AopUtil.findFirstAnnotatedParameterOfTypeOrThrow(joinPoint, ProjectId.class, Long.class);
        permissionService.checkProjectPermission(AuthenticationUtil.getCurrentUser().getId(),
                projectId, Arrays.asList(requireProjectPermission.value()));
    }

    /**
     * Finds the team ID from the given join point.
     *
     * <p>Searches for the team ID in the following order:</p>
     * <ol>
     *   <li>Searches for a Long parameter annotated with {@link TeamId}</li>
     *   <li>If not found, retrieves the associated team ID using a {@link ProjectId} annotated project ID</li>
     * </ol>
     *
     * @param joinPoint The join point of the currently executing method
     * @return The found team ID
     * @throws IllegalStateException If no Long parameter with TeamId or ProjectId annotation can be found
     */
    private Long findTeamId(JoinPoint joinPoint) {
        Optional<Long> teamId = AopUtil.findFirstAnnotatedParameterOfType(joinPoint, TeamId.class, Long.class);
        if (teamId.isPresent()) {
            return teamId.get();
        }

        Optional<Long> projectId = AopUtil.findFirstAnnotatedParameterOfType(joinPoint, ProjectId.class, Long.class);
        if (projectId.isPresent()) {
            return projectService.getAssociatedTeamForProject(projectId.get()).getId();
        }

        throw new IllegalStateException("'%s' 혹은 '%s' 애노테이션이 붙은 '%s' 타입의 파라미터를 찾을 수 없습니다."
                .formatted(TeamId.class.getSimpleName(), ProjectId.class.getSimpleName(), Long.class.getSimpleName()));
    }
}
