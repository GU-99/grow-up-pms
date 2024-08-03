package com.growup.pms.project.repository;

import com.growup.pms.project.domain.ProjectUser;
import com.growup.pms.project.domain.ProjectUserId;
import com.growup.pms.role.domain.Permission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, ProjectUserId> {
    @Query("""
            SELECT p FROM ProjectUser pu
            JOIN pu.role r
            JOIN r.rolePermissions rp
            JOIN rp.permission p
            WHERE pu.project.id = :teamId AND pu.user.id = :userId
            """)
    List<Permission> getPermissions(@Param("projectId") Long projectId, @Param("userId") Long userId);
}
