package com.growup.pms.status.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;

@Builder
public record StatusOrderListEditRequest(@Valid @NotEmpty List<StatusOrderEditRequest> statuses) {
}
