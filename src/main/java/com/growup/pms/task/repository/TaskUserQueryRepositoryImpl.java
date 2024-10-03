package com.growup.pms.task.repository;

import static com.growup.pms.project.domain.QProjectUser.projectUser;
import static com.growup.pms.role.domain.QRole.role;
import static com.growup.pms.task.domain.QTask.task;
import static com.growup.pms.task.domain.QTaskUser.taskUser;
import static com.growup.pms.user.domain.QUser.user;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.task.controller.dto.response.TaskUserResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TaskUserQueryRepositoryImpl implements TaskUserQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TaskUserResponse> getTaskUsersByProjectIdAndTaskId(Long projectId, Long taskId) {
        return queryFactory.select(Projections.constructor(TaskUserResponse.class,
                        taskUser.user.id,
                        taskUser.user.profile.nickname,
                        role.name
                ))
                .from(taskUser)
                .join(taskUser.task, task)
                .join(taskUser.user, user)
                .join(projectUser).on(projectUser.user.eq(taskUser.user))
                .join(projectUser.project).on(isProjectId(projectId))
                .join(projectUser.role, role)
                .where(isTaskId(taskId))
                .fetch();
    }

    private static BooleanExpression isProjectId(Long projectId) {
        if (projectId == null) {
            throw new BusinessException(ErrorCode.INVALID_PROJECT);
        }
        return projectUser.project.id.eq(projectId);
    }

    private static BooleanExpression isTaskId(Long taskId) {
        return taskId != null ? taskUser.task.id.eq(taskId) : null;
    }
}
