package com.growup.pms.team.service;

import com.growup.pms.team.domain.Team;
import com.growup.pms.team.dto.TeamCreateDto;
import com.growup.pms.team.dto.TeamResponse;
import com.growup.pms.team.dto.TeamUpdateDto;
import com.growup.pms.team.repository.TeamRepository;
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
    public Long createTeam(Long creatorId, TeamCreateDto request) {
        return teamRepository.save(TeamCreateDto.toEntity(request, userRepository.findByIdOrThrow(creatorId)))
                .getId();
    }

    @Transactional
    public void deleteTeam(Long teamId) {
        teamRepository.deleteById(teamId);
    }

    @Transactional
    public void updateTeam(Long teamId, TeamUpdateDto request) {
        Team team = teamRepository.findByIdOrThrow(teamId);
        if (request.getName().isPresent()) {
            team.updateName(request.getName().get());
        }
        if (request.getContent().isPresent()) {
            team.updateContent(request.getContent().get());
        }
    }
}
