package com.growup.pms.file.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.IoUtils;
import software.amazon.awssdk.utils.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileStorageService implements FileStorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String upload(MultipartFile multipartFile) {
        log.info("Attempting to upload file: {}", multipartFile.getOriginalFilename());
        validateFileExists(multipartFile);

        String uniqueFileName = generateUniqueFileName(multipartFile.getOriginalFilename());
        File tempFile = convertMultiPartToFile(multipartFile);

        try {
            uploadFileToS3(tempFile, uniqueFileName);
            log.info("Successfully uploaded file: {} to S3", uniqueFileName);
            return uniqueFileName;
        } catch (S3Exception e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED, e)
                    .addDetail("fileName", multipartFile.getOriginalFilename());
        } finally {
            if (!tempFile.delete()) {
                log.warn("Failed to delete temporary file: {}", tempFile.getAbsolutePath());
            }
        }
    }

    @Override
    public byte[] download(String filePath) {
        log.info("Attempting to download file: {}", filePath);
        try (ResponseInputStream<GetObjectResponse> content = s3Client.getObject(builder -> builder.bucket(bucketName)
                .key(filePath))) {
            return IoUtils.toByteArray(content);
        } catch (IOException | S3Exception e) {
            throw new BusinessException(ErrorCode.FILE_DOWNLOAD_FAILED, e)
                    .addDetail("filePath", filePath);
        }
    }

    @Override
    public void delete(String filePath) {
        log.info("Attempting to delete file: {}", filePath);
        try {
            s3Client.deleteObject(builder -> builder.bucket(bucketName)
                    .key(filePath));
        } catch (S3Exception e) {
            throw new BusinessException(ErrorCode.FILE_DELETE_FAILED, e)
                    .addDetail("filePath", filePath);
        }
    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new BusinessException(ErrorCode.EMPTY_FILE);
        }

        if (StringUtils.isEmpty(multipartFile.getOriginalFilename())) {
            throw new BusinessException(ErrorCode.MISSING_FILE_NAME);
        }
    }

    private File convertMultiPartToFile(MultipartFile multipartFile) {
        try {
            // 파일명의 접두사가 temp_고 접미사가 .tmp 인 임시 파일을 생성한다.
            File tempFile = File.createTempFile("temp_", ".tmp");
            try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                outputStream.write(multipartFile.getBytes());
            }
            return tempFile;
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_CONVERSION_FAILED, e)
                    .addDetail("fileName", multipartFile.getOriginalFilename());
        }
    }

    private void uploadFileToS3(File file, String fileName) {
        s3Client.putObject(builder -> builder.bucket(bucketName)
                .key(fileName), RequestBody.fromFile(file));
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        return UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        return lastDotIndex == -1 ? "" : filename.substring(lastDotIndex + 1);
    }
}
