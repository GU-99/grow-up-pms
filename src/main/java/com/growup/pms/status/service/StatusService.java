package com.growup.pms.status.service;

import com.growup.pms.status.controller.dto.response.StatusResponse;
import com.growup.pms.status.repository.StatusRepository;
import com.growup.pms.status.service.dto.StatusCreateDto;
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
    public StatusResponse createStatus(StatusCreateDto dto) {
        return null;
    }
}
