package com.growup.pms.task.repository;

import static com.growup.pms.status.domain.QStatus.status;
import static com.growup.pms.task.domain.QTask.task;
import static com.growup.pms.user.domain.QUser.user;

import com.growup.pms.task.controller.dto.response.TaskResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TaskQueryRepositoryImpl implements TaskQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TaskResponse> getTasksByStatusId(Long statusId) {
        List<Long> ids = queryFactory
                .select(task.id)
                .from(task)
                .leftJoin(task.status, status)
                .leftJoin(task.user, user)
                .where(
                        isStatusId(statusId)
                )
                .orderBy(task.sortOrder.asc())
                .fetch();

        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        return queryFactory
                .select(Projections.constructor(TaskResponse.class,
                        task.id,
                        task.status.id,
                        task.name,
                        task.user.username,
                        task.sortOrder
                ))
                .from(task)
                .leftJoin(task.status, status)
                .leftJoin(task.user, user)
                .where(
                        task.id.in(ids)
                )
                .orderBy(task.sortOrder.asc())
                .fetch();
    }

    private BooleanExpression isStatusId(Long statusId) {
        return statusId != null ? task.status.id.eq(statusId) : task.status.isNull();
    }
}
