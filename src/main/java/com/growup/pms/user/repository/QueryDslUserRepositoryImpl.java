package com.growup.pms.user.repository;

import static com.growup.pms.role.domain.QRole.role;
import static com.growup.pms.team.domain.QTeam.team;
import static com.growup.pms.team.domain.QTeamUser.teamUser;
import static com.growup.pms.user.domain.QUser.user;

import com.growup.pms.user.controller.dto.response.UserSearchResponse;
import com.growup.pms.user.controller.dto.response.UserTeamResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QueryDslUserRepositoryImpl implements QueryDslUserRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UserSearchResponse> findUsersByNicknameStartingWith(String nicknamePrefix) {
        return jpaQueryFactory.select(Projections.constructor(
                        UserSearchResponse.class,
                        user.id,
                        user.profile.nickname
                ))
                .from(user)
                .where(user.profile.nickname.startsWithIgnoreCase(nicknamePrefix))
                .orderBy(user.profile.nickname.asc())
                .limit(5)
                .fetch();
    }

    @Override
    public List<UserTeamResponse> findAllUserTeams(Long userId) {
        return jpaQueryFactory.select(Projections.constructor(
                        UserTeamResponse.class,
                        teamUser.team.id,
                        team.name,
                        team.content,
                        user.profile.nickname,
                        user.id,
                        teamUser.isPendingApproval,
                        role.name
                ))
                .from(teamUser)
                .join(teamUser.team, team)
                .join(teamUser.role, role)
                .join(team.creator, user)
                .where(teamUser.user.id.eq(userId))
                .fetch();
    }
}
