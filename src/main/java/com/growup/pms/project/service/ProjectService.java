package com.growup.pms.project.service;

import com.growup.pms.common.util.PeriodValidator;
import com.growup.pms.project.controller.dto.response.ProjectResponse;
import com.growup.pms.project.domain.Project;
import com.growup.pms.project.domain.ProjectUser;
import com.growup.pms.project.domain.ProjectUserId;
import com.growup.pms.project.repository.ProjectRepository;
import com.growup.pms.project.repository.ProjectUserRepository;
import com.growup.pms.project.service.dto.ProjectCreateCommand;
import com.growup.pms.project.service.dto.ProjectEditCommand;
import com.growup.pms.project.service.dto.ProjectUserCreateCommand;
import com.growup.pms.role.domain.ProjectRole;
import com.growup.pms.role.domain.Role;
import com.growup.pms.role.repository.RoleRepository;
import com.growup.pms.status.repository.StatusRepository;
import com.growup.pms.task.repository.TaskRepository;
import com.growup.pms.team.domain.Team;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import java.time.LocalDate;
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
    private final StatusRepository statusRepository;
    private final TaskRepository taskRepository;

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

    public Team getAssociatedTeamForProject(Long projectId) {
        return projectRepository.findByIdOrThrow(projectId).getTeam();
    }

    @Transactional
    public void editProject(Long projectId, ProjectEditCommand command) {
        Project project = projectRepository.findByIdOrThrow(projectId);

        editFieldIfPresent(command.projectName(), (v, p) -> p.editName(v.get()), project);
        editFieldIfPresent(command.content(), (v, p) -> p.editContent(v.get()), project);
        editStartDateIfPresent(command.startDate(), (v, p) -> p.editStartDate(v.get()), project);
        editEndDateIfPresent(command.endDate(), (v, p) -> p.editEndDate(v.get()), project);
    }

    private <T> void editFieldIfPresent(JsonNullable<T> value, BiConsumer<JsonNullable<T>, Project> updater,
                                        Project project) {
        value.ifPresent(v -> updater.accept(JsonNullable.of(v), project));
    }

    private void editStartDateIfPresent(JsonNullable<LocalDate> startDate, BiConsumer<JsonNullable<LocalDate>, Project> updater,
                                        Project project) {
        startDate.ifPresent(v -> {
                LocalDate earliestTaskStartDate = taskRepository.getEarliestStartDateInProject(project.getId());
                PeriodValidator.validateProjectStartBeforeAllTaskStart(v, earliestTaskStartDate);
                updater.accept(JsonNullable.of(v), project);
            });
    }

    private void editEndDateIfPresent(JsonNullable<LocalDate> endDate, BiConsumer<JsonNullable<LocalDate>, Project> updater,
                                   Project project) {
        endDate.ifPresent(v -> {
            LocalDate latestTaskEndDate = taskRepository.getLatestEndDateInProject(project.getId());
            PeriodValidator.validateProjectEndAfterAllTaskEnd(v, latestTaskEndDate);
            updater.accept(JsonNullable.of(v), project);
        });
    }

    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findByIdOrThrow(projectId);
        projectRepository.delete(project);
    }

    @Transactional
    public void leaveProject(Long teamId, Long projectId, Long userId) {
        Team team = teamRepository.findByIdOrThrow(teamId);
        ProjectUser projectUser = projectUserRepository.findByIdOrThrow(new ProjectUserId(projectId, userId));
        // TODO: 팀장이 탈퇴하는 경우에 대한 로직을 결정해야함
        if (projectUser.getRole().getName().equals(ProjectRole.ADMIN.getRoleName())) {
            if (userId.equals(team.getCreator().getId())) {
                throw new UnsupportedOperationException("아직 구현되지 않은 기능입니다.");
            }
        }
        projectUserRepository.delete(projectUser);
    }

    @Transactional
    public void deleteAllProjectsInTeam(Long teamId) {
        List<Long> projectIds = projectRepository.getProjectIdsByTeamId(teamId);
        projectIds.forEach(projectId -> {
            statusRepository.findAllByProjectId(projectId)
                    .forEach(taskRepository::deleteAllByStatus);
            statusRepository.deleteAllByProjectId(projectId);
            projectUserRepository.deleteAllByProjectId(projectId);
        });
        projectRepository.deleteAllByIdInBatch(projectIds);
    }
}
