package com.growup.pms.user.controller.dto.request;

import com.growup.pms.common.validation.constraint.ImageFile;
import com.growup.pms.user.service.dto.UserUploadCommand;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UserUploadRequest(@NotNull @ImageFile MultipartFile file) {
    public UserUploadCommand toCommand() {
        return new UserUploadCommand(file);
    }
}
