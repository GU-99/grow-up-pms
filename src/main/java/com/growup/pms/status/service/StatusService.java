package com.growup.pms.status.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.project.domain.Project;
import com.growup.pms.project.repository.ProjectRepository;
import com.growup.pms.status.controller.dto.response.StatusResponse;
import com.growup.pms.status.domain.Status;
import com.growup.pms.status.repository.StatusRepository;
import com.growup.pms.status.service.dto.StatusCreateCommand;
import com.growup.pms.status.service.dto.StatusEditCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatusService {

    private final StatusRepository statusRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public StatusResponse createStatus(StatusCreateCommand command) {
        Project project = getProject(command);
        Status savedStatus = statusRepository.save(command.toEntity(project));

        return StatusResponse.of(savedStatus);
    }

    public List<StatusResponse> getStatuses(Long projectId) {
        return null;
    }

    public void editStatus(StatusEditCommand dto) {
    }

    public void editStatusOrder(Long statusId, Short sortOrder) {
    }

    public void deleteStatus(Long statusId) {
    }

    private Project getProject(StatusCreateCommand command) {
        return projectRepository.findById(command.projectId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PROJECT_NOT_FOUND));
    }
}
