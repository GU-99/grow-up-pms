package com.growup.pms.invitation.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.DuplicateException;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.invitation.domian.TeamInvitation;
import com.growup.pms.invitation.dto.TeamInvitationCreateRequest;
import com.growup.pms.invitation.repository.TeamInvitationRepository;
import com.growup.pms.team.domain.TeamUserId;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.team.repository.TeamUserRepository;
import com.growup.pms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamInvitationService {
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;
    private final TeamInvitationRepository teamInvitationRepository;

    @Transactional
    public Long sendInvitation(Long teamIdToInvite, TeamInvitationCreateRequest request) {
        if (isUserAlreadyInTeam(teamIdToInvite, request.getUserId())) {
            throw new DuplicateException(ErrorCode.USER_ALREADY_IN_TEAM);
        }

        TeamInvitation invitation = TeamInvitationCreateRequest.toEntity(
                userRepository.getReferenceById(request.getUserId()),
                teamRepository.getReferenceById(teamIdToInvite));
        try {
            return teamInvitationRepository.save(invitation).getId();
        } catch (DataIntegrityViolationException ex) {
            throw new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND);
        }
    }

    private boolean isUserAlreadyInTeam(Long teamIdToInvite, Long userIdToInvite) {
        return teamUserRepository.existsById(new TeamUserId(teamIdToInvite, userIdToInvite));
    }
}
