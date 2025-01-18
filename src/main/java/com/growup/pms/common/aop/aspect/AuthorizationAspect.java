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
     * 팀 관련 권한을 검사하는 어드바이스입니다.
     * {@link RequireTeamPermission} 애노테이션이 붙은 메서드 실행 전에 호출됩니다.
     *
     * <p>메서드 파라미터에서 {@link TeamId} 애노테이션이 붙은 팀 ID를 찾아 권한을 검사합니다.
     * 만약 팀 ID를 찾을 수 없는 경우, {@link ProjectId} 애노테이션이 붙은 프로젝트 ID를 통해
     * 연관된 팀을 찾아 권한을 검사합니다.</p>
     *
     * @param joinPoint 현재 실행 중인 메서드의 조인 포인트
     * @param requireTeamPermission 요구되는 팀 권한 정보를 담고 있는 애노테이션
     * @throws BusinessException 사용자가 필요한 권한이 없는 경우
     */
    @Before(value = "@annotation(requireTeamPermission)", argNames = "joinPoint,requireTeamPermission")
    public void checkTeamPermission(JoinPoint joinPoint, RequireTeamPermission requireTeamPermission) {
        Long teamId = findTeamId(joinPoint);
        permissionService.checkTeamPermission(AuthenticationUtil.getCurrentUser().getId(),
                teamId, Arrays.asList(requireTeamPermission.value()));
    }

    /**
     * 프로젝트 관련 권한을 검사하는 어드바이스입니다.
     * {@link RequireProjectPermission} 애노테이션이 붙은 메서드 실행 전에 호출됩니다.
     *
     * <p>메서드 파라미터에서 {@link ProjectId} 애노테이션이 붙은 프로젝트 ID를 찾아
     * 현재 사용자가 해당 프로젝트에 대해 요구되는 권한을 가지고 있는지 검사합니다.</p>
     *
     * @param joinPoint 현재 실행 중인 메서드의 조인 포인트
     * @param requireProjectPermission 요구되는 프로젝트 권한 정보를 담고 있는 애노테이션
     * @throws BusinessException 사용자가 필요한 권한이 없는 경우
     * @throws IllegalStateException ProjectId 애노테이션이 붙은 Long 타입 파라미터를 찾을 수 없는 경우
     */
    @Before(value = "@annotation(requireProjectPermission)", argNames = "joinPoint,requireProjectPermission")
    public void checkProjectPermission(JoinPoint joinPoint, RequireProjectPermission requireProjectPermission) {
        Long projectId = AopUtil.findFirstAnnotatedParameterOfTypeOrThrow(joinPoint, ProjectId.class, Long.class);
        permissionService.checkProjectPermission(AuthenticationUtil.getCurrentUser().getId(),
                projectId, Arrays.asList(requireProjectPermission.value()));
    }

    /**
     * 주어진 조인 포인트에서 팀 ID를 찾는 헬퍼 메서드입니다.
     * 
     * <p>다음 순서로 팀 ID를 찾습니다:</p>
     * <ol>
     *   <li>{@link TeamId} 애노테이션이 붙은 Long 타입 파라미터 검색</li>
     *   <li>없는 경우, {@link ProjectId} 애노테이션이 붙은 프로젝트 ID를 통해 연관된 팀 ID 조회</li>
     * </ol>
     *
     * @param joinPoint 현재 실행 중인 메서드의 조인 포인트
     * @return 찾아낸 팀 ID
     * @throws IllegalStateException TeamId 또는 ProjectId 애노테이션이 붙은 Long 타입 파라미터를 찾을 수 없는 경우
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
