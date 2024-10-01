package com.growup.pms.project.service;

import com.growup.pms.project.controller.dto.response.ProjectResponse;
import com.growup.pms.project.domain.Project;
import com.growup.pms.project.domain.ProjectUser;
import com.growup.pms.project.repository.ProjectRepository;
import com.growup.pms.project.repository.ProjectUserRepository;
import com.growup.pms.project.service.dto.ProjectCreateCommand;
import com.growup.pms.project.service.dto.ProjectEditCommand;
import com.growup.pms.project.service.dto.ProjectUserCreateCommand;
import com.growup.pms.role.domain.ProjectRole;
import com.growup.pms.role.domain.Role;
import com.growup.pms.role.repository.RoleRepository;
import com.growup.pms.team.domain.Team;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import java.util.List;
import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final ProjectUserRepository projectUserRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public Long createProject(Long teamId, Long projectCreatorId, ProjectCreateCommand command) {
        Team team = teamRepository.findByIdOrThrow(teamId);
        Project savedProject = projectRepository.save(command.toEntity(team));

        addProjectAdmin(projectCreatorId, team, savedProject);

        inviteProjectUsers(savedProject, command.coworkers());

        return savedProject.getId();
    }

    private void addProjectAdmin(Long projectCreatorId, Team team, Project savedProject) {
        User projectCreator = userRepository.findByIdOrThrow(projectCreatorId);
        Role adminRole = roleRepository.findProjectRoleByName(ProjectRole.ADMIN.getRoleName());
        User teamCreator = team.getCreator();
        if (!projectCreator.equals(teamCreator)) {
            projectUserRepository.save(createProjectUser(teamCreator, savedProject, adminRole));
        }
        projectUserRepository.save(createProjectUser(projectCreator, savedProject, adminRole));
    }

    private ProjectUser createProjectUser(User user, Project savedProject, Role adminRole) {
        return ProjectUser.builder()
                .user(user)
                .project(savedProject)
                .role(adminRole)
                .build();
    }

    private void inviteProjectUsers(Project project, List<ProjectUserCreateCommand> commands) {
        List<ProjectUser> projectUsers = commands.stream()
                .map(command -> createAssignees(project, command))
                .toList();
        projectUserRepository.saveAll(projectUsers);
    }

    private ProjectUser createAssignees(Project project, ProjectUserCreateCommand command) {
        User user = userRepository.findByIdOrThrow(command.userId());
        Role role = roleRepository.findProjectRoleByName(command.roleName());

        return command.toEntity(project, user, role);
    }

    public List<ProjectResponse> getProjects(Long teamId) {
        return projectRepository.getProjectsByTeamId(teamId);
    }

    @Transactional
    public void editProject(Long projectId, ProjectEditCommand command) {
        Project project = projectRepository.findByIdOrThrow(projectId);

        editFieldIfPresent(command.projectName(), (v,t) -> t.editName(v.get()), project);
        editFieldIfPresent(command.content(), (v, t) -> t.editContent(v.get()), project);
        editFieldIfPresent(command.startDate(), (v, t) -> t.editStartDate(v.get()), project);
        editFieldIfPresent(command.endDate(), (v, t) -> t.editEndDate(v.get()), project);
    }

    private <T> void editFieldIfPresent(JsonNullable<T> value, BiConsumer<JsonNullable<T>, Project> updater, Project project) {
        value.ifPresent(v -> updater.accept(JsonNullable.of(v), project));
    }

    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findByIdOrThrow(projectId);
        projectRepository.delete(project);
    }

    public void deleteAllProjectsForTeam(Long teamId) {
        throw new UnsupportedOperationException("아직 구현되지 않은 기능입니다.");
    }
}
