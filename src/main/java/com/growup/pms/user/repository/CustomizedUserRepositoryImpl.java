package com.growup.pms.user.repository;

import static com.growup.pms.user.domain.QUser.user;

import com.growup.pms.user.controller.dto.response.UserSearchResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomizedUserRepositoryImpl implements CustomizedUserRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<UserSearchResponse> findUsersByUsernameStartingWith(String nicknamePrefix) {
        return buildUserSearchProjection().from(user)
                .where(user.profile.nickname.startsWithIgnoreCase(nicknamePrefix))
                .orderBy(user.profile.nickname.asc())
                .limit(5)
                .fetch();
    }

    private JPAQuery<UserSearchResponse> buildUserSearchProjection() {
        return jpaQueryFactory.select(Projections.constructor(
            UserSearchResponse.class,
            user.id,
            user.profile.nickname
        ));
    }
}
