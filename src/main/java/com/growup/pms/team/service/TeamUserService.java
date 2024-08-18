package com.growup.pms.team.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.AuthorizationException;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.role.domain.Role;
import com.growup.pms.role.domain.TeamRole;
import com.growup.pms.team.domain.TeamUserId;
import com.growup.pms.team.repository.TeamUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamUserService {
    private final TeamUserRepository teamUserRepository;

    @Transactional
    public void kickMember(Long teamId, Long targetMemberId) {
        Role targetMemberRole = teamUserRepository.findRoleById(teamId, targetMemberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TEAM_MEMBER_NOT_FOUND));

        ensureMemberIsMate(targetMemberRole);

        teamUserRepository.deleteById(new TeamUserId(teamId, targetMemberId));
    }

    private void ensureMemberIsMate(Role targetMemberRole) {
        if (!TeamRole.MATE.equals(TeamRole.of(targetMemberRole.getName()))) {
            throw new AuthorizationException(ErrorCode.AUTHZ_ACCESS_DENIED);
        }
    }
}
