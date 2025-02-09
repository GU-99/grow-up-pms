package com.growup.pms.task.repository;

import static com.growup.pms.project.domain.QProject.project;
import static com.growup.pms.status.domain.QStatus.status;
import static com.growup.pms.task.domain.QTask.task;

import com.growup.pms.task.controller.dto.response.TaskResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
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

    public void decreaseSortOrderByStatus(Long statusId, Short sortOrder) {
        queryFactory.update(task)
                .set(task.sortOrder, task.sortOrder.subtract(1))
                .where(isStatusId(statusId), task.sortOrder.gt(sortOrder))
                .execute();
    }

    public List<TaskResponse> getAllTasksByStatus(Long statusId) {
        List<Long> ids = queryFactory.select(task.id)
                .from(task)
                .join(task.status, status)
                .where(
                        isStatusId(statusId)
                )
                .orderBy(task.sortOrder.asc())
                .fetch();

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        return queryFactory.select(Projections.constructor(TaskResponse.class,
                        task.id,
                        task.status.id,
                        task.name,
                        task.content,
                        task.sortOrder,
                        task.period.startDate,
                        task.period.endDate
                ))
                .from(task)
                .join(task.status, status)
                .where(
                        task.id.in(ids)
                )
                .orderBy(task.sortOrder.asc())
                .fetch();
    }

    public LocalDate getEarliestStartDateInProject(Long projectId) {
        return queryFactory.select(task.period.startDate)
                .from(task)
                .join(task.status, status)
                .join(task.status.project, project)
                .where(
                        project.id.eq(projectId),
                        task.period.startDate.isNotNull()
                )
                .orderBy(task.period.startDate.asc())
                .fetchFirst();
    }

    public LocalDate getLatestEndDateInProject(Long projectId) {
        return queryFactory.select(task.period.endDate)
                .from(task)
                .join(task.status, status)
                .join(task.status.project, project)
                .where(
                        project.id.eq(projectId),
                        task.period.endDate.isNotNull()
                )
                .orderBy(task.period.endDate.desc())
                .fetchFirst();
    }

    private BooleanExpression isStatusId(Long statusId) {
        return statusId != null ? task.status.id.eq(statusId) : null;
    }
}
