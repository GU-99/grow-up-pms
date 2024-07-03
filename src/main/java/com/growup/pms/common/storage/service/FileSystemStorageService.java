package com.growup.pms.common.storage.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.StorageException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
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
        if (file.isEmpty()) {
            throw new StorageException(ErrorCode.STORAGE_EMPTY_FILE_ERROR);
        }

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
}
