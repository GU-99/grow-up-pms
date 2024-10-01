package com.growup.pms.project.repository;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
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
            WHERE pu.project.id = :projectId AND pu.user.id = :userId
            """)
    List<Permission> getPermissionsForProjectUser(@Param("projectId") Long projectId, @Param("userId") Long userId);
    default ProjectUser findByIdOrThrow(ProjectUserId id) {
        return findById(id).orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_USER_NOT_FOUND));
    }
}
