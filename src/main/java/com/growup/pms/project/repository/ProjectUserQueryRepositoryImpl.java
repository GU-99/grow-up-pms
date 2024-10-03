package com.growup.pms.project.repository;

import static com.growup.pms.project.domain.QProject.project;
import static com.growup.pms.project.domain.QProjectUser.projectUser;
import static com.growup.pms.role.domain.QRole.role;
import static com.growup.pms.user.domain.QUser.user;

import com.growup.pms.project.controller.dto.response.ProjectUserResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProjectUserQueryRepositoryImpl implements ProjectUserQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProjectUserResponse> getProjectUsersByProjectId(Long projectId) {
        return queryFactory.select(Projections.constructor(ProjectUserResponse.class,
                projectUser.user.id,
                projectUser.user.profile.nickname,
                projectUser.role.name
                ))
                .from(projectUser)
                .join(projectUser.user, user)
                .join(projectUser.project, project)
                .join(projectUser.role, role)
                .where(isProjectId(projectId))
                .fetch();
    }

    private BooleanExpression isProjectId(Long projectId) {
        return projectId != null ? projectUser.project.id.eq(projectId) : null;
    }
}