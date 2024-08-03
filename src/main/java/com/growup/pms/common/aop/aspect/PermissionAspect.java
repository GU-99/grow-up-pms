package com.growup.pms.common.aop.aspect;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.common.aop.annotation.RequirePermission;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.AuthorizationException;
import com.growup.pms.project.repository.ProjectUserRepository;
import com.growup.pms.role.domain.Permission;
import com.growup.pms.team.repository.TeamUserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {
    private final TeamUserRepository teamUserRepository;
    private final ProjectUserRepository projectUserRepository;

    @Before("@annotation(requirePermission) && args(teamId, ..)")
    public void checkTeamPermission(RequirePermission requirePermission, Long teamId) {
        checkPermission(requirePermission, teamUserRepository.getPermissions(teamId, getCurrentUser().getId()));
    }

    @Before("@annotation(requirePermission) && args(projectId, ..)")
    public void checkProjectPermission(RequirePermission requirePermission, Long projectId) {
        checkPermission(requirePermission, projectUserRepository.getPermissions(projectId, getCurrentUser().getId()));
    }

    private void checkPermission(RequirePermission requirePermission, List<Permission> permissions) {
        Set<String> permissionNames = permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

        boolean hasAllPermissions = Arrays.stream(requirePermission.value())
                .map(Enum::name)
                .allMatch(permissionNames::contains);

        if (!hasAllPermissions) {
            throw new AuthorizationException(ErrorCode.AUTHZ_ACCESS_DENIED);
        }
    }

    private SecurityUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new AuthorizationException(ErrorCode.AUTHZ_ACCESS_DENIED);
        }
        return (SecurityUser) authentication.getPrincipal();
    }
}
