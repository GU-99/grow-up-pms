package com.growup.pms.task.service.dto;

import lombok.Builder;

@Builder
public record TaskOrderEditCommand(Long statusId, Long taskId, Short sortOrder) {

}
