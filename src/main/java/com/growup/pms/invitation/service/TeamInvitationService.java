package com.growup.pms.invitation.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.DuplicateException;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.invitation.domian.TeamInvitation;
import com.growup.pms.invitation.domian.dto.TeamInvitationCreateCommand;
import com.growup.pms.invitation.repository.TeamInvitationRepository;
import com.growup.pms.role.domain.TeamRole;
import com.growup.pms.role.repository.RoleRepository;
import com.growup.pms.team.domain.TeamUser;
import com.growup.pms.team.domain.TeamUserId;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.team.repository.TeamUserRepository;
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
    private final TeamInvitationRepository teamInvitationRepository;

    @Transactional
    public Long sendInvitation(Long teamId, TeamInvitationCreateCommand command) {
        if (isUserAlreadyInTeam(teamId, command.userId())) {
            throw new DuplicateException(ErrorCode.USER_ALREADY_IN_TEAM);
        }

        TeamInvitation invitation = TeamInvitationCreateCommand.toEntity(
                userRepository.findByIdOrThrow(command.userId()),
                teamRepository.findByIdOrThrow(teamId));

        return teamInvitationRepository.save(invitation).getId();
    }

    @Transactional
    public void acceptInvitation(Long teamId, Long userId) {
        TeamInvitation invitation = teamInvitationRepository.findUserInvitationForTeam(teamId, userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        TeamUser newTeamUser = createTeamUser(invitation);

        teamInvitationRepository.delete(invitation);
        teamUserRepository.save(newTeamUser);
    }

    @Transactional
    public void declineInvitation(Long teamId, Long userId) {
        teamInvitationRepository.declineUserInvitationForTeam(teamId, userId);
    }

    private boolean isUserAlreadyInTeam(Long teamId, Long userId) {
        return teamUserRepository.existsById(new TeamUserId(teamId, userId));
    }

    private TeamUser createTeamUser(TeamInvitation invitation) {
        return TeamUser.builder()
                .team(invitation.getTeam())
                .role(roleRepository.findByNameOrThrow(TeamRole.MATE.getRoleName()))
                .user(invitation.getUser())
                .build();
    }
}
