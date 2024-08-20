package com.growup.pms.task.repository;

import static com.growup.pms.project.domain.QProject.project;
import static com.growup.pms.status.domain.QStatus.status;
import static com.growup.pms.task.domain.QTask.task;
import static com.growup.pms.user.domain.QUser.user;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.growup.pms.task.controller.dto.response.TaskResponse;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Long> ids = queryFactory
                .select(task.id)
                .from(task)
                .join(task.user, user)
                .join(task.status, status)
                .join(task.status.project, project)
                .where(
                        projectId != null ? task.status.project.id.eq(projectId) : null
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
                .join(task.user, user)
                .join(task.status, status)
                .join(task.status.project, project)
                .where(
                        task.id.in(ids)
                )
                .orderBy(task.sortOrder.asc())
                .fetch();

        return results.stream()
                .sorted(Comparator.comparingInt(t -> t.get(1, TaskResponse.class).sortOrder()))
                .collect(
                        groupingBy(
                                tuple -> tuple.get(1, TaskResponse.class).statusId(),
                                Collectors.mapping(
                                        tuple -> tuple.get(1, TaskResponse.class),
                                        toList()
                                )
                        )
                );
    }
}
