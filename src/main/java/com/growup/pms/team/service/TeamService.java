package com.growup.pms.team.service;

import com.growup.pms.team.controller.dto.response.TeamResponse;
import com.growup.pms.team.domain.Team;
import com.growup.pms.team.repository.TeamRepository;
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
    public void deleteTeam(Long teamId) {
        teamRepository.deleteById(teamId);
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
}
