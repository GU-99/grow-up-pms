package com.growup.pms.invitation.repository;

import com.growup.pms.invitation.domian.TeamInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, Long> {
}
