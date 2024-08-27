package com.growup.pms.common.storage.service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
class FileSystemStorageServiceTest {

    @Autowired
    FileSystemStorageService fileSystemStorageService;

    @Test
    void 파일을_업로드한다() throws Exception {
        // given
        final String rootPath = "src/test/resources/images";
        final String fileName = "testImage.jpg";
        FileInputStream fileInputStream = new FileInputStream(rootPath + "/" + fileName);

        MockMultipartFile file = new MockMultipartFile(
            "test",
            fileName,
            MediaType.IMAGE_JPEG_VALUE,
            fileInputStream
        );

        // when
        String uploadFileName = fileSystemStorageService.upload(file, "images");

        // then
        Path sourceFilePath = Paths.get(rootPath).resolve(fileName).normalize().toAbsolutePath();
        Path destinationFilePath = Paths.get(rootPath).resolve(uploadFileName).normalize().toAbsolutePath();

        String sourceBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(sourceFilePath));
        String destinationBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(destinationFilePath));

        Assertions.assertEquals(sourceBase64, destinationBase64);
    }

    @Test
    void 파일의_리소스를_가져온다() throws Exception {
        // given
        final String rootPath = "src/test/resources";
        final String filePath = "images/testImage.jpg";

        // when
        Resource resource = fileSystemStorageService.getFileResource(filePath);

        // then
        Path sourceFilePath = Paths.get(rootPath).resolve(filePath).normalize().toAbsolutePath();
        String sourceBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(sourceFilePath));

        String base64 = Base64.getEncoder().encodeToString(resource.getInputStream().readAllBytes());
        Assertions.assertEquals(sourceBase64, base64);
    }

    @AfterEach
    void removeFile() {
        File rootFolder = new File("src/test/resources/images");
        File[] files = rootFolder.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {
            if (!file.getName().equals("testImage.jpg")) {
                file.delete();
            }
        }
    }
}
