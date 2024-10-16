package com.growup.pms.task.repository;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.task.domain.TaskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment, Long>, TaskAttachmentQueryRepository {

    default TaskAttachment findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new BusinessException(ErrorCode.FILE_DELETE_FAILED));
    }
}
