package com.growup.pms.task.repository;

import static com.growup.pms.project.domain.QProject.project;
import static com.growup.pms.status.domain.QStatus.status;
import static com.growup.pms.task.domain.QTask.task;
import static com.growup.pms.user.domain.QUser.user;
import static java.util.stream.Collectors.groupingBy;

import com.growup.pms.task.controller.dto.response.TaskResponse;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TaskQueryRepositoryImpl implements TaskQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, List<TaskResponse>> getTasksByProjectId(Long projectId) {
        // TODO: 성능테스트 후 쿼리 수정에 대한 논의 필요함
        //  projectId 를 통해 statusId 리스트를 만들고 taskId 리스트를 추출 후 조회하는 방식
        List<Long> ids = queryFactory
                .select(task.id)
                .from(task)
                .leftJoin(task.user, user)
                .join(task.status, status)
                .join(task.status.project, project)
                .where(
                        isProjectId(projectId)
                )
                .orderBy(task.sortOrder.asc())
                .fetch();

        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }

        List<Tuple> results = queryFactory
                .select(task.status.id, Projections.constructor(TaskResponse.class,
                        task.id,
                        task.status.id,
                        task.name,
                        task.user.username,
                        task.sortOrder
                ))
                .from(task)
                .leftJoin(task.user, user)
                .join(task.status, status)
                .join(task.status.project, project)
                .where(
                        task.id.in(ids)
                )
                .orderBy(task.sortOrder.asc())
                .fetch();

        return results.stream()
                .map(tuple -> tuple.get(1, TaskResponse.class))
                .collect(groupingBy(task -> Objects.requireNonNull(task).statusId()));
    }

    private BooleanExpression isProjectId(Long projectId) {
        return projectId != null ? task.status.project.id.eq(projectId) : null;
    }
}
