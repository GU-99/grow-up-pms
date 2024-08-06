package com.growup.pms.team.repository;

import com.growup.pms.role.domain.Permission;
import com.growup.pms.team.domain.TeamUser;
import com.growup.pms.team.domain.TeamUserId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamUserRepository extends JpaRepository<TeamUser, TeamUserId> {
    @Query("""
            SELECT p FROM TeamUser tu
            JOIN tu.role r
            JOIN r.rolePermissions rp
            JOIN rp.permission p
            WHERE tu.team.id = :teamId AND tu.user.id = :userId
            """)
    List<Permission> getPermissionsForTeamUser(@Param("teamId") Long teamId, @Param("userId") Long userId);
}
