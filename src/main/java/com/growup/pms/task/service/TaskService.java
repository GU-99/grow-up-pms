package com.growup.pms.task.service;

import com.growup.pms.task.controller.dto.response.TaskDetailResponse;
import com.growup.pms.task.service.dto.TaskCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    public TaskDetailResponse createTask(TaskCreateDto dto) {
        return null;
    }
}
