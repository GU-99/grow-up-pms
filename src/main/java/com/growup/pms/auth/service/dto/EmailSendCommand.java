package com.growup.pms.auth.service.dto;

import lombok.Builder;

@Builder
public record EmailSendCommand(String recipient, String subject, String content) {
}
