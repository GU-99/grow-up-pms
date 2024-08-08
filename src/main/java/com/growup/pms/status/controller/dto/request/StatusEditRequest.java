package com.growup.pms.status.controller.dto.request;

import com.growup.pms.common.util.EncryptionUtil;
import com.growup.pms.status.service.dto.StatusEditCommand;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusEditRequest {

    @Size(min = 1, max = 32)
    private JsonNullable<String> statusName = JsonNullable.undefined();

    @Size(min = 1, max = 6)
    private JsonNullable<String> colorCode = JsonNullable.undefined();

    private JsonNullable<Short> sortOrder = JsonNullable.undefined();

    @Builder
    public StatusEditRequest(JsonNullable<String> statusName, JsonNullable<String> colorCode,
                             JsonNullable<Short> sortOrder) {
        this.statusName = statusName;
        this.colorCode = colorCode;
        this.sortOrder = sortOrder;
    }

    public StatusEditCommand toCommand(String statusId) {
        return StatusEditCommand.builder()
                .statusId(EncryptionUtil.decrypt(statusId))
                .statusName(statusName)
                .colorCode(colorCode)
                .sortOrder(sortOrder)
                .build();
    }
}
