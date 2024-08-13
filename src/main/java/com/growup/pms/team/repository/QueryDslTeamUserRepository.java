package com.growup.pms.team.repository;

import com.growup.pms.role.domain.Permission;
import java.util.List;
import org.springframework.data.repository.query.Param;

public interface QueryDslTeamUserRepository {
    List<Permission> getPermissionsForTeamUser(@Param("teamId") Long teamId, @Param("userId") Long userId);
}
