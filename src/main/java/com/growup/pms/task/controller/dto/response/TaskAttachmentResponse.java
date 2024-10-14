package com.growup.pms.task.controller.dto.response;

import lombok.Builder;

@Builder
public record TaskAttachmentResponse(Long fileId, String fileName, String uploadName) {

}
