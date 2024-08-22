package com.growup.pms.team.repository;

import com.growup.pms.role.domain.Role;
import com.growup.pms.team.domain.TeamUser;
import com.growup.pms.team.domain.TeamUserId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TeamUserRepository extends JpaRepository<TeamUser, TeamUserId>, QueryDslTeamUserRepository {
    @Query("""
        SELECT r
        FROM TeamUser tu JOIN tu.role r ON tu.role.id = r.id
        WHERE tu.team.id = :teamId AND tu.user.id = :userId""")
    Optional<Role> findRoleById(Long teamId, Long userId);

    @Modifying
    @Query("""
        UPDATE TeamUser tu SET tu.isPendingApproval = false
        WHERE tu.team.id = :teamId AND tu.user.id = :userId AND tu.isPendingApproval = true""")
    int acceptInvitation(Long teamId, Long userId);

    @Modifying
    @Query("""
        DELETE FROM TeamUser tu
        WHERE tu.team.id = :teamId AND tu.user.id = :userId AND tu.isPendingApproval = true""")
    int declineInvitation(Long teamId, Long userId);

    void deleteAllByTeamId(Long teamId);
}
