package com.growup.pms.team.repository;

import com.growup.pms.role.domain.Permission;
import java.util.List;

public interface QueryDslTeamUserRepository {
    List<Permission> getPermissionsForTeamUser(Long teamId, Long userId);

    long updateRoleForTeamUser(Long teamId, Long userId, String newRoleName);
}
