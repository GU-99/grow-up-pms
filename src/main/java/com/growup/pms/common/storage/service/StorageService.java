package com.growup.pms.common.storage.service;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    UUID upload(MultipartFile file, String path);
}
