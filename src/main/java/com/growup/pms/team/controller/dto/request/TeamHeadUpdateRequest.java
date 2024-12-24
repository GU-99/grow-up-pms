package com.growup.pms.team.controller.dto.request;

import jakarta.validation.constraints.Positive;

public record TeamHeadUpdateRequest(@Positive Long userId) {
}
