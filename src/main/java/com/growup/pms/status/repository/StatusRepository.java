package com.growup.pms.status.repository;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.status.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long>, StatusQueryRepository {

    default Status findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.STATUS_NOT_FOUND));
    }
}
