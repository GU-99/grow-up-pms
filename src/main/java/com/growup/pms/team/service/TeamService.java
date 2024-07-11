package com.growup.pms.team.service;

import com.growup.pms.team.dto.TeamCreateRequest;
import com.growup.pms.team.dto.TeamResponse;
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
}
