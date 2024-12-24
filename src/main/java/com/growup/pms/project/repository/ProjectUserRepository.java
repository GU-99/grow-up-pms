package com.growup.pms.project.repository;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.project.domain.ProjectUser;
import com.growup.pms.project.domain.ProjectUserId;
import com.growup.pms.role.domain.Permission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, ProjectUserId>, ProjectUserQueryRepository {

    @Query("""
            SELECT p FROM ProjectUser pu
            JOIN pu.role r
            JOIN r.rolePermissions rp
            JOIN rp.permission p
            WHERE pu.project.id = :projectId AND pu.user.id = :userId
            """)
    List<Permission> getPermissionsForProjectUser(Long projectId, Long userId);

    @Modifying
    @Query("""
            DELETE FROM ProjectUser pu
            WHERE pu.user.id = :userId AND pu.project.id IN
            (SELECT p.id FROM Project p JOIN Team t ON p.team.id = t.id WHERE t.id = :teamId)
            """)
    void deleteMemberFromAllProjects(Long teamId, Long userId);

    @Modifying
    @Query(value = """
        INSERT INTO project_users(project_id, user_id, role_id)
        SELECT p.id, :userId, :roleId
        FROM projects p
        WHERE p.team_id = :teamId
        ON DUPLICATE KEY UPDATE role_id = :roleId""", nativeQuery = true)
    void upsertTeamRole(Long teamId, Long userId, Long roleId);

    default ProjectUser findByIdOrThrow(ProjectUserId id) {
        return findById(id).orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_USER_NOT_FOUND));
    }
}
