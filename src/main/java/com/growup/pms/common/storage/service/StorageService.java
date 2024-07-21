package com.growup.pms.common.storage.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String upload(MultipartFile file, String path);
}
