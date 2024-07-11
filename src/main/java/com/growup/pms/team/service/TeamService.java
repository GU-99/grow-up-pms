package com.growup.pms.team.service;

import com.growup.pms.team.domain.Team;
import com.growup.pms.team.dto.TeamCreateRequest;
import com.growup.pms.team.dto.TeamResponse;
import com.growup.pms.team.dto.TeamUpdateRequest;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamResponse getTeam(Long teamId) {
        return TeamResponse.from(teamRepository.findByIdOrThrow(teamId));
    }

    public Long createTeam(TeamCreateRequest request) {
        return teamRepository.save(TeamCreateRequest.toEntity(request, userRepository.findByIdOrThrow(request.getCreatorId())))
                .getId();
    }

    public void deleteTeam(Long teamId) {
        teamRepository.deleteById(teamId);
    }

    public void updateTeam(Long teamId, TeamUpdateRequest request) {
        Team team = teamRepository.findByIdOrThrow(teamId);
        if (request.getName().isPresent()) {
            team.updateName(request.getName().get());
        }
        if (request.getContent().isPresent()) {
            team.updateContent(request.getContent().get());
        }
        teamRepository.save(team);
    }
}
