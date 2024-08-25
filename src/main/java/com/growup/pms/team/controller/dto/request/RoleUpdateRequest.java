package com.growup.pms.team.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record RoleUpdateRequest(@NotNull @Length(min = 1, max = 64) String role) {
}
