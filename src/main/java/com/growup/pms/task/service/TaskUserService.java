package com.growup.pms.task.service;

import com.growup.pms.task.controller.dto.response.TaskUserResponse;
import com.growup.pms.task.domain.Task;
import com.growup.pms.task.domain.TaskUser;
import com.growup.pms.task.domain.TaskUserId;
import com.growup.pms.task.repository.TaskRepository;
import com.growup.pms.task.repository.TaskUserRepository;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskUserService {

    private final TaskUserRepository taskUserRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createTaskUser(Long taskId, Long userId) {
        Task task = taskRepository.findByIdOrThrow(taskId);
        User user = userRepository.findByIdOrThrow(userId);
        TaskUser taskUser = TaskUser.builder()
                .task(task)
                .user(user)
                .build();

        taskUserRepository.save(taskUser);
    }

    public List<TaskUserResponse> getAssignees(Long projectId, Long taskId) {
        return taskUserRepository.getTaskUsersByProjectIdAndTaskId(projectId, taskId);
    }

    @Transactional
    public void deleteTaskUser(Long taskId, Long assigneeId) {
        TaskUser taskUser = taskUserRepository.findByIdOrThrow(new TaskUserId(taskId, assigneeId));
        taskUserRepository.delete(taskUser);
    }
}
