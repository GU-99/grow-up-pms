package com.growup.pms.team.repository;

import com.growup.pms.role.domain.Permission;
import com.growup.pms.team.controller.dto.response.TeamUserResponse;
import com.growup.pms.team.controller.dto.response.TeamUserSearchResponse;
import java.util.List;

public interface QueryDslTeamUserRepository {
    List<Permission> getPermissionsForTeamUser(Long teamId, Long userId);

    List<TeamUserResponse> getAllTeamUsers(Long teamId);

    List<TeamUserSearchResponse> getTeamUsersByNicknameStartingWith(Long userId, Long teamId, String nicknamePrefix);

    long updateRoleForTeamUser(Long teamId, Long userId, String newRoleName);
}
