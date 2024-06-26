package com.growup.pms.common.aop.aspect;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.common.aop.annotation.RequirePermission;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.AuthorizationException;
import com.growup.pms.role.domain.Permission;
import com.growup.pms.role.domain.PermissionType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {

    @Before("@annotation(requirePermission) && args(teamId, ..)")
    public void checkTeamPermission(RequirePermission requirePermission, Long teamId) {
        Long userId = ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        PermissionType[] requiredPermissions = requirePermission.permissions();
        // FIXME: 향후 팀 유저 기능이 구현되면 우항을 변경해야 함
        List<Permission> teamUserPermissions = Collections.emptyList();

        Set<String> userPermissionNames = teamUserPermissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

        boolean hasAllPermissions = Arrays.stream(requiredPermissions)
                .map(Enum::name)
                .allMatch(userPermissionNames::contains);

        if (!hasAllPermissions) {
            throw new AuthorizationException(ErrorCode.AUTHZ_ACCESS_DENIED);
        }
    }
}
