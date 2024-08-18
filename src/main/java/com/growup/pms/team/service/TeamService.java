package com.growup.pms.team.service;

import com.growup.pms.invitation.repository.TeamInvitationRepository;
import com.growup.pms.project.service.ProjectService;
import com.growup.pms.team.controller.dto.response.TeamResponse;
import com.growup.pms.team.domain.Team;
import com.growup.pms.team.domain.TeamUserId;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.team.repository.TeamUserRepository;
import com.growup.pms.team.service.dto.TeamCreateCommand;
import com.growup.pms.team.service.dto.TeamUpdateCommand;
import com.growup.pms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamUserRepository teamUserRepository;
    private final TeamInvitationRepository teamInvitationRepository;
    private final ProjectService projectService;

    public TeamResponse getTeam(Long teamId) {
        return TeamResponse.from(teamRepository.findByIdOrThrow(teamId));
    }

    @Transactional
    // TODO: 권한이나 역할이 구현되면 팀 생성 시 coworker 필드에 있는 팀원들을 팀 멤버 테이블에 추가해야 함
    public Long createTeam(Long creatorId, TeamCreateCommand command) {
        return teamRepository.save(command.toEntity(userRepository.findByIdOrThrow(creatorId)))
                .getId();
    }

    @Transactional
    public void leaveTeam(Long teamId, Long userId) {
        if (teamRepository.isUserTeamLeader(teamId, userId)) {
            removeTeam(teamId);
        } else {
            teamUserRepository.deleteById(new TeamUserId(teamId, userId));
        }
    }

    @Transactional
    public void updateTeam(Long teamId, TeamUpdateCommand command) {
        Team team = teamRepository.findByIdOrThrow(teamId);
        if (command.name().isPresent()) {
            team.updateName(command.name().get());
        }
        if (command.content().isPresent()) {
            team.updateContent(command.content().get());
        }
    }

    private void removeTeam(Long teamId) {
        projectService.deleteAllProjectsForTeam(teamId);
        teamUserRepository.deleteAllByTeamId(teamId);
        teamInvitationRepository.deleteAllByTeamId(teamId);
    }
}
