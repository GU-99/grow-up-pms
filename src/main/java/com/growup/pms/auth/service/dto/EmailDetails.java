package com.growup.pms.auth.service.dto;

import lombok.Builder;

@Builder
public record EmailDetails(String recipient, String subject, String content) {
}
