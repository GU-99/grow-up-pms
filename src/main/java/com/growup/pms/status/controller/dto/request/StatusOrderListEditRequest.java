package com.growup.pms.status.controller.dto.request;

import com.growup.pms.status.service.dto.StatusOrderEditCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;

@Builder
public record StatusOrderListEditRequest(@Valid @NotEmpty List<StatusOrderEditRequest> statuses) {

    public List<StatusOrderEditCommand> toCommands() {
        return statuses.stream().map(StatusOrderEditRequest::toCommand).toList();
    }
}
