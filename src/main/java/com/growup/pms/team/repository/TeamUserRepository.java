package com.growup.pms.team.repository;

import com.growup.pms.team.domain.TeamUser;
import com.growup.pms.team.domain.TeamUserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamUserRepository extends JpaRepository<TeamUser, TeamUserId>, QueryDslTeamUserRepository {
    void deleteAllByTeamId(Long teamId);
}
