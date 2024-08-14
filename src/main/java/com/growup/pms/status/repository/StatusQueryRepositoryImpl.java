package com.growup.pms.status.repository;

import static com.growup.pms.status.domain.QStatus.status;

import com.growup.pms.status.controller.dto.response.StatusResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StatusQueryRepositoryImpl implements StatusQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<StatusResponse> getAllStatusByProjectId(Long projectId) {
        List<Long> ids = queryFactory
                .select(status.id)
                .from(status)
                .where(
                        isProjectId(projectId)
                )
                .fetch();

        log.info("ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        return queryFactory
                .select(Projections.constructor(StatusResponse.class,
                        status.id,
                        status.project.id,
                        status.name,
                        status.colorCode,
                        status.sortOrder
                ))
                .from(status)
                .where(
                        status.id.in(ids)
                )
                .orderBy(status.sortOrder.asc())
                .fetch();
    }

    private BooleanExpression isProjectId(Long projectId) {
        return projectId != null ? status.project.id.eq(projectId) : null;
    }
}
