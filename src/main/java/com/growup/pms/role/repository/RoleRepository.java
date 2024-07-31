package com.growup.pms.role.repository;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.role.domain.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    default Role findByNameOrThrow(String name) {
        return findByName(name).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
    }
}
