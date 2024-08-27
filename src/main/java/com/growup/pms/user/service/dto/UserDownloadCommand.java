package com.growup.pms.user.service.dto;

import org.springframework.core.io.Resource;

public record UserDownloadCommand(String imageName, Resource resource) {
}
