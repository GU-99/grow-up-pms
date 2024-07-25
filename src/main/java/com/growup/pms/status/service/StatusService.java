package com.growup.pms.status.service;

import com.growup.pms.status.repository.StatusRepository;
import com.growup.pms.status.service.dto.CreateStatusDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StatusService {

    private final StatusRepository statusRepository;

    public Long createStatus(CreateStatusDto dto) {
        return null;
    }
}
