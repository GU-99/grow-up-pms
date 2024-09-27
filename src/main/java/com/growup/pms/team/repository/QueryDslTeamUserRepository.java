package com.growup.pms.team.repository;

import com.growup.pms.role.domain.Permission;
import com.growup.pms.team.controller.dto.response.TeamUserResponse;
import java.util.List;

public interface QueryDslTeamUserRepository {
    List<Permission> getPermissionsForTeamUser(Long teamId, Long userId);

    List<TeamUserResponse> getAllTeamUsers(Long teamId);

    long updateRoleForTeamUser(Long teamId, Long userId, String newRoleName);
}
