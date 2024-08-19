package com.growup.pms.task.repository;

import com.growup.pms.task.controller.dto.response.TaskResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TaskQueryRepositoryImpl implements TaskQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, List<TaskResponse>> getTasksByProjectId(Long statusId) {
        return null;
    }
}
