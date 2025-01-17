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

    /**
     * Checks if a user has the required permissions for a specific team.
     *
     * @param userId The unique identifier of the user whose permissions are being checked
     * @param teamId The unique identifier of the team for which permissions are being validated
     * @param requestedTeamPermissions A list of team-specific permissions being requested
     * @throws BusinessException if the user does not have all the requested permissions for the team
     */
    public void checkTeamPermission(Long userId, Long teamId, List<TeamPermission> requestedTeamPermissions) {
        List<Permission> grantedPermissions = teamUserRepository.getPermissionsForTeamUser(teamId, userId);
        validatePermissions(requestedTeamPermissions, grantedPermissions);
    }

    /**
     * Checks if a user has the required permissions for a specific project.
     *
     * @param userId The unique identifier of the user whose permissions are being checked
     * @param projectId The unique identifier of the project for which permissions are being validated
     * @param requestedProjectPermissions A list of project-specific permissions being requested
     * @throws BusinessException if the user does not have all the requested project permissions
     */
    public void checkProjectPermission(Long userId, Long projectId, List<ProjectPermission> requestedProjectPermissions) {
        List<Permission> grantedPermissions = projectUserRepository.getPermissionsForProjectUser(projectId, userId);
        validatePermissions(requestedProjectPermissions, grantedPermissions);
    }

    /**
     * Validates that a user has all requested permissions by checking against their granted permissions.
     *
     * @param requestedPermissions A list of requested permissions to validate, extending BasePermission
     * @param grantedPermissions A list of permissions already granted to the user
     * @throws BusinessException if the user does not have all requested permissions, with an ACCESS_DENIED error code
     */
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
