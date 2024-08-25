package com.growup.pms.team.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
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
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_MEMBER_NOT_FOUND));

        ensureMemberIsMate(targetMemberRole);

        teamUserRepository.deleteById(new TeamUserId(teamId, targetMemberId));
    }

    @Transactional
    public void changeRole(Long teamId, Long targetMemberId, String roleName) {
        ensureValidTeamRole(roleName);

        teamUserRepository.updateRoleForTeamUser(teamId, targetMemberId, roleName);
    }

    private void ensureValidTeamRole(String roleName) {
        if (!(TeamRole.MATE.getRoleName().equals(roleName) || TeamRole.LEADER.getRoleName().equals(roleName))) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ROLE_ASSIGNMENT);
        }
    }

    private void ensureMemberIsMate(Role targetMemberRole) {
        if (!TeamRole.MATE.equals(TeamRole.of(targetMemberRole.getName()))) {
            throw new BusinessException(ErrorCode.AUTHZ_ACCESS_DENIED);
        }
    }
}
