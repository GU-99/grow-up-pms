package com.growup.pms.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String upload(MultipartFile multipartFile);

    byte[] download(String filePath);

    void delete(String filePath);
}
