package com.growup.pms.role.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.project.repository.ProjectUserRepository;
import com.growup.pms.role.domain.BasePermission;
import com.growup.pms.role.domain.Permission;
import com.growup.pms.role.domain.ProjectPermission;
import com.growup.pms.role.domain.TeamPermission;
import com.growup.pms.team.repository.TeamUserRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final TeamUserRepository teamUserRepository;
    private final ProjectUserRepository projectUserRepository;

    public void checkTeamPermission(Long userId, Long teamId, List<TeamPermission> requestedTeamPermissions) {
        List<Permission> grantedPermissions = teamUserRepository.getPermissionsForTeamUser(teamId, userId);
        validatePermissions(requestedTeamPermissions, grantedPermissions);
    }

    public void checkProjectPermission(Long userId, Long projectId, List<ProjectPermission> requestedProjectPermissions) {
        List<Permission> grantedPermissions = projectUserRepository.getPermissionsForProjectUser(projectId, userId);
        validatePermissions(requestedProjectPermissions, grantedPermissions);
    }

    private void validatePermissions(List<? extends BasePermission> requestedPermissions, List<Permission> grantedPermissions) {
        Set<String> grantedPermissionNames = grantedPermissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

        boolean hasAllPermissions = requestedPermissions.stream()
                .allMatch(permission -> grantedPermissionNames.contains(permission.getFullName()));
        if (!hasAllPermissions) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }
}
