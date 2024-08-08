package com.growup.pms.status.controller.dto.response;

import com.growup.pms.common.util.EncryptionUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusResponse {

    private String statusId;
    private String projectId;
    private String name;
    private String colorCode;
    private Short sortOrder;

    @Builder
    public StatusResponse(Long statusId, Long projectId, String name, String colorCode, Short sortOrder) {
        this.statusId = EncryptionUtil.encrypt(String.valueOf(statusId));
        this.projectId = EncryptionUtil.encrypt(String.valueOf(projectId));
        this.name = name;
        this.colorCode = colorCode;
        this.sortOrder = sortOrder;
    }
}
