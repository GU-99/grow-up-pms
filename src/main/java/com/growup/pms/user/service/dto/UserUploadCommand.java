package com.growup.pms.user.service.dto;

import org.springframework.web.multipart.MultipartFile;

public record UserUploadCommand(MultipartFile file) {
}
