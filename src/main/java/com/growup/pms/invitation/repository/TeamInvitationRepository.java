package com.growup.pms.invitation.repository;

import com.growup.pms.invitation.domian.TeamInvitation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, Long> {
    @Query("SELECT t FROM TeamInvitation t WHERE t.user.id = :userId AND t.team.id = :teamId")
    Optional<TeamInvitation> findUserInvitationForTeam(@Param("teamId") Long teamId, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM TeamInvitation t WHERE t.user.id = :userId AND t.team.id = :teamId")
    void declineUserInvitationForTeam(@Param("teamId") Long teamId, @Param("userId") Long userId);

    void deleteAllByTeamId(Long teamId);
}
