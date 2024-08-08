package com.growup.pms.status.service;

import com.growup.pms.status.controller.dto.response.PageResponse;
import com.growup.pms.status.controller.dto.response.StatusResponse;
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

    @Transactional
    public StatusResponse createStatus(StatusCreateCommand dto) {
        return null;
    }

    public PageResponse<List<StatusResponse>> getStatuses(String projectId) {
        return null;
    }

    public void editStatus(StatusEditCommand dto) {
    }

    public void editStatusOrder(Long statusId, Short sortOrder) {
    }

    public void deleteStatus(String statusId) {
    }
}
