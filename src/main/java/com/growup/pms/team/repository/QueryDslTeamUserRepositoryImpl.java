package com.growup.pms.team.repository;

import static com.growup.pms.role.domain.QPermission.permission;
import static com.growup.pms.role.domain.QRole.role;
import static com.growup.pms.role.domain.QRolePermission.rolePermission;
import static com.growup.pms.team.domain.QTeamUser.teamUser;

import com.growup.pms.role.domain.Permission;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QueryDslTeamUserRepositoryImpl implements QueryDslTeamUserRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Permission> getPermissionsForTeamUser(Long teamId, Long userId) {
        return jpaQueryFactory.select(permission)
                .from(teamUser)
                .join(teamUser.role, role)
                .join(role.rolePermissions, rolePermission)
                .join(rolePermission.permission, permission)
                .where(teamUser.team.id.eq(teamId), teamUser.user.id.eq(userId))
                .fetch();
    }

    @Override
    public long updateRoleForTeamUser(Long teamId, Long userId, String newRoleName) {
        Long roleId = jpaQueryFactory.select(role.id)
                .from(role)
                .where(role.name.eq(newRoleName))
                .fetchOne();

        if (roleId == null) {
            throw new IllegalArgumentException("해당 역할은 존재하지 않습니다: %s".formatted(newRoleName));
        }

        return jpaQueryFactory.update(teamUser)
                .set(teamUser.role.id, roleId)
                .where(teamUser.team.id.eq(teamId), teamUser.user.id.eq(userId))
                .execute();
    }
}
