package com.growup.pms.status.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StatusQueryRepository {

    private final JPAQueryFactory queryFactory;

}
