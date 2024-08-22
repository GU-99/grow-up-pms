package com.growup.pms.team.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.DuplicateException;
import com.growup.pms.common.exception.exceptions.InvalidInputException;
import com.growup.pms.role.domain.TeamRole;
import com.growup.pms.role.repository.RoleRepository;
import com.growup.pms.team.domain.TeamUser;
import com.growup.pms.team.domain.TeamUserId;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.team.repository.TeamUserRepository;
import com.growup.pms.team.service.dto.TeamInvitationCreateCommand;
import com.growup.pms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamInvitationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;

    @Transactional
    public void sendInvitation(Long teamId, TeamInvitationCreateCommand command) {
        validateTeamInvitation(teamId, command);

        TeamUser invitedUser = TeamUser.builder()
                .user(userRepository.findByIdOrThrow(command.userId()))
                .team(teamRepository.findByIdOrThrow(teamId))
                .role(roleRepository.findTeamRoleByName(command.roleName()))
                .isPendingApproval(true)
                .build();

        teamUserRepository.save(invitedUser);
    }

    @Transactional
    public void acceptInvitation(Long teamId, Long userId) {
        teamUserRepository.acceptInvitation(teamId, userId);
    }

    @Transactional
    public void declineInvitation(Long teamId, Long userId) {
        teamUserRepository.declineInvitation(teamId, userId);
    }

    private void validateTeamInvitation(Long teamId, TeamInvitationCreateCommand command) {
        if (isUserAlreadyInTeam(teamId, command.userId())) {
            throw new DuplicateException(ErrorCode.USER_ALREADY_IN_TEAM);
        }

        if (!(TeamRole.MATE.getRoleName().equals(command.roleName())
                || TeamRole.LEADER.getRoleName().equals(command.roleName()))) {
            throw new InvalidInputException(ErrorCode.UNAUTHORIZED_ROLE_ASSIGNMENT);
        }
    }

    private boolean isUserAlreadyInTeam(Long teamId, Long userId) {
        return teamUserRepository.existsById(new TeamUserId(teamId, userId));
    }
}
