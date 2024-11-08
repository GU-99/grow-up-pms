package com.growup.pms.team.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.project.service.ProjectService;
import com.growup.pms.role.domain.Role;
import com.growup.pms.role.domain.RoleType;
import com.growup.pms.role.domain.TeamRole;
import com.growup.pms.role.repository.RoleRepository;
import com.growup.pms.team.controller.dto.response.TeamResponse;
import com.growup.pms.team.domain.Team;
import com.growup.pms.team.domain.TeamUser;
import com.growup.pms.team.domain.TeamUserId;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.team.repository.TeamUserRepository;
import com.growup.pms.team.service.dto.TeamCreateCommand;
import com.growup.pms.team.service.dto.TeamCreateCommand.TeamCoworkerCommand;
import com.growup.pms.team.service.dto.TeamUpdateCommand;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
    private final ProjectService projectService;
    private final RoleRepository roleRepository;

    public TeamResponse getTeam(Long teamId) {
        return TeamResponse.from(teamRepository.findByIdOrThrow(teamId));
    }

    @Transactional
    public Long createTeam(Long creatorId, TeamCreateCommand command) {
        validateTeamCreation(creatorId, command);

        User creator = userRepository.findByIdOrThrow(creatorId);
        Team team = teamRepository.save(command.toEntity(creator));

        inviteAllUsersToTeam(team, command.coworkers());
        return team.getId();
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

        if (command.teamName().isPresent()) {
            team.updateName(command.teamName().get());
        }
        if (command.content().isPresent()) {
            team.updateContent(command.content().get());
        }
    }

    private void inviteAllUsersToTeam(Team newTeam, List<TeamCoworkerCommand> coworkers) {
        Map<String, Role> roles = roleRepository.findByType(RoleType.TEAM).stream()
                .collect(Collectors.toMap(
                        Role::getName,
                        role -> role
                ));

        List<TeamUser> invitedUsers = new ArrayList<>();
        invitedUsers.add(TeamUser.builder()
                .user(newTeam.getCreator())
                .role(roles.get(TeamRole.HEAD.getRoleName()))
                .team(newTeam)
                .isPendingApproval(false)
                .build());

        for (var coworker : coworkers) {
            if (!roles.containsKey(coworker.roleName())) {
                continue;
            }

            invitedUsers.add(TeamUser.builder()
                    .team(newTeam)
                    .user(userRepository.findByIdOrThrow(coworker.userId()))
                    .role(roles.get(coworker.roleName()))
                    .isPendingApproval(true)
                    .build());
        }
        teamUserRepository.saveAll(invitedUsers);
    }

    private void removeTeam(Long teamId) {
        projectService.deleteAllProjectsForTeam(teamId);
        teamUserRepository.deleteAllByTeamId(teamId);
    }

    private void validateTeamCreation(Long creatorId, TeamCreateCommand command) {
        validateTeamNameDuplication(command.teamName());
        validateNoDuplicateMembers(creatorId, command.coworkers());
        validateMemberRoles(command.coworkers());
    }

    private void validateTeamNameDuplication(String teamName) {
        if (teamRepository.existsByName(teamName)) {
            throw new BusinessException(ErrorCode.TEAM_NAME_DUPLICATED);
        }
    }

    private void validateNoDuplicateMembers(Long creatorId, List<TeamCoworkerCommand> coworkers) {
        Set<Long> userIds = coworkers.stream()
                .map(TeamCoworkerCommand::userId)
                .collect(Collectors.toSet());
        if (userIds.size() != coworkers.size() || userIds.contains(creatorId)) {
            throw new BusinessException(ErrorCode.INVALID_DATA_FORMAT);
        }
    }

    private void validateMemberRoles(List<TeamCoworkerCommand> coworkers) {
        boolean hasHead = coworkers.stream()
                .anyMatch(c -> TeamRole.HEAD.getRoleName().equals(c.roleName()));
        if (hasHead) {
            throw new BusinessException(ErrorCode.INVALID_DATA_FORMAT);
        }
    }
}
