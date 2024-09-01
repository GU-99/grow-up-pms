package com.growup.pms.user.service.dto;

import lombok.Builder;

@Builder
public record RecoverCommand(String email, String verificationCode) {
}
