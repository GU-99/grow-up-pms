package com.growup.pms.user.service.dto;

import lombok.Builder;

@Builder
public record UserPasswordUpdateCommand(String password, String passwordNew) {
}