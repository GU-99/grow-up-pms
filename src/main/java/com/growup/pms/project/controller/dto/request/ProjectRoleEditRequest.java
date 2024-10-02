package com.growup.pms.project.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ProjectRoleEditRequest(@NotNull @Size(min = 1, max = 64) String roleName) {

}
