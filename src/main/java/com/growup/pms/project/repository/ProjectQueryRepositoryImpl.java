package com.growup.pms.project.repository;

import static com.growup.pms.project.domain.QProject.project;
import static com.growup.pms.team.domain.QTeam.team;

import com.growup.pms.project.controller.dto.response.ProjectResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProjectQueryRepositoryImpl implements ProjectQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProjectResponse> getProjectsByTeamId(Long teamId) {
        List<Long> ids = queryFactory
                .select(project.id)
                .from(project)
                .join(project.team, team)
                .where(
                        isTeamId(teamId)
                )
                .fetch();

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        return queryFactory
                .select(Projections.constructor(ProjectResponse.class,
                        project.id,
                        project.name,
                        project.content,
                        project.startDate,
                        project.endDate,
                        project.createdAt,
                        project.updatedAt
                ))
                .from(project)
                .join(project.team, team)
                .where(
                        project.id.in(ids)
                )
                .fetch();
    }

    private BooleanExpression isTeamId(Long teamId) {
        return teamId != null ? project.team.id.eq(teamId) : null;
    }
}
