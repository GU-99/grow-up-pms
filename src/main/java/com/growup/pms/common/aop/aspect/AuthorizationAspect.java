package com.growup.pms.common.aop.aspect;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.common.aop.annotation.ProjectId;
import com.growup.pms.common.aop.annotation.RequirePermission;
import com.growup.pms.common.aop.annotation.TeamId;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.common.util.AopUtil;
import com.growup.pms.project.repository.ProjectUserRepository;
import com.growup.pms.role.domain.Permission;
import com.growup.pms.team.repository.TeamUserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 이 클래스는 팀과 프로젝트에 대한 사용자 권한을 검사합니다. {@link RequirePermission}
 * 애노테이션이 붙은 메서드에 대해 권한 검사를 수행합니다.</p>
 *
 * <p><strong>사용 예시:</strong></p>
 * <pre>{@code
 * @DeleteMapping("/{teamId}")
 * @RequirePermission(PermissionType.TEAM_DELETE)
 * public ResponseEntity<Void> deleteTeam(@TeamId @PathVariable Long teamId) {
 *     // 메서드 구현
 * }
 * }</pre>
 *
 * @see RequirePermission
 * @see TeamId
 * @see ProjectId
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {
    private final TeamUserRepository teamUserRepository;
    private final ProjectUserRepository projectUserRepository;

    @Pointcut("@annotation(requirePermission)")
    public void annotatedWithRequirePermission(RequirePermission requirePermission) {}

    @Pointcut("execution(public * *(.., @com.growup.pms.common.aop.annotation.TeamId (*), ..))")
    public void methodWithTeamIdParameter() {}

    @Pointcut("execution(public * *(.., @com.growup.pms.common.aop.annotation.ProjectId (*), ..))")
    public void methodWithProjectIdParameter() {}

    @Before(value = "annotatedWithRequirePermission(requirePermission) && methodWithTeamIdParameter()", argNames = "joinPoint,requirePermission")
    public void checkTeamPermission(JoinPoint joinPoint, RequirePermission requirePermission) {
        Long teamId = AopUtil.findFirstAnnotatedParameterOfType(joinPoint, TeamId.class, Long.class);
        checkPermission(requirePermission, teamUserRepository.getPermissionsForTeamUser(teamId, getCurrentUser().getId()));
    }

    @Before(value = "annotatedWithRequirePermission(requirePermission) && methodWithProjectIdParameter()", argNames = "joinPoint,requirePermission")
    public void checkProjectPermission(JoinPoint joinPoint, RequirePermission requirePermission) {
        Long projectId = AopUtil.findFirstAnnotatedParameterOfType(joinPoint, ProjectId.class, Long.class);
        checkPermission(requirePermission, projectUserRepository.getPermissionsForProjectUser(projectId, getCurrentUser().getId()));
    }

    private void checkPermission(RequirePermission requirePermission, List<Permission> grantedPermissions) {
        Set<String> permissionNames = grantedPermissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

        boolean hasAllPermissions = Arrays.stream(requirePermission.value())
                .map(Enum::name)
                .allMatch(permissionNames::contains);

        if (!hasAllPermissions) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }

    private SecurityUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        return (SecurityUser) authentication.getPrincipal();
    }
}
