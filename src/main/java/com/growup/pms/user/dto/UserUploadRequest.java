package com.growup.pms.user.dto;

import com.growup.pms.common.validation.constraint.ImageFile;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUploadRequest {
    @NotNull
    @ImageFile
    private MultipartFile file;

    public UserUploadRequest(MultipartFile file) {
        this.file = file;
    }

    public static UserUploadDto toServiceDto(UserUploadRequest request) {
        return new UserUploadDto(request.getFile());
    }
}
