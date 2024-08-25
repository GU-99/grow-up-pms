package com.growup.pms.role.repository;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.role.domain.Role;
import com.growup.pms.role.domain.RoleType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByTypeAndName(RoleType type, String name);


    default Role findByTypeAndNameOrThrow(RoleType type, String name) {
        return findByTypeAndName(type, name)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
    }

    default Role findTeamRoleByName(String name) {
        return findByTypeAndNameOrThrow(RoleType.TEAM, name);
    }

    default Role findProjectRoleByName(String name) {
        return findByTypeAndNameOrThrow(RoleType.PROJECT, name);
    }
}
