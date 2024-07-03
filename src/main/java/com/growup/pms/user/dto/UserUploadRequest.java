package com.growup.pms.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUploadRequest {
    private Long userId;

    private MultipartFile file;

    @Builder
    public UserUploadRequest(Long userId, MultipartFile file) {
        this.userId = userId;
        this.file = file;
    }
}
