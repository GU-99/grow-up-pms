package com.growup.pms.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUploadDto {
    private MultipartFile file;

    public UserUploadDto(MultipartFile file) {
        this.file = file;
    }
}
