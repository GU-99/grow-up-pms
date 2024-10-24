package com.growup.pms.task.repository;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.task.domain.TaskUser;
import com.growup.pms.task.domain.TaskUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskUserRepository extends JpaRepository<TaskUser, TaskUserId>, TaskUserQueryRepository {

    default TaskUser findByIdOrThrow(TaskUserId id) {
        return findById(id).orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_USER_NOT_FOUND));
    }
}
