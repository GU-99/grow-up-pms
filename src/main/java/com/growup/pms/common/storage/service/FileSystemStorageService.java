package com.growup.pms.common.storage.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.StorageException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootPath;

    public FileSystemStorageService(
        @Value("${storage.root-path}") String rootPath
    ) {
        this.rootPath = Paths.get(rootPath);
    }

    @Override
    public String upload(MultipartFile file, String path) {
        UUID uploadFileName = UUID.randomUUID();
        Path filePath = Paths.get(path).resolve(uploadFileName.toString());
        Path destinationPath = this.rootPath.resolve(filePath).normalize().toAbsolutePath();

        if (!Files.exists(destinationPath.getParent())) {
            try {
                Files.createDirectories(destinationPath.getParent());
            } catch (IOException e) {
                throw new StorageException(ErrorCode.STORAGE_CREATE_FOLDER_ERROR);
            }
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException(ErrorCode.STORAGE_STORE_ERROR);
        }

        return uploadFileName.toString();
    }

    @Override
    public Resource getFileResource(String path) {
        Path filePath = this.rootPath.resolve(path).normalize().toAbsolutePath();

        if (!Files.exists(filePath)) {
            throw new StorageException(ErrorCode.STORAGE_NOT_FOUND_ERROR);
        }

        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new StorageException(ErrorCode.STORAGE_READE_FILE_ERROR);
        }
    }
}
