package com.growup.pms.user.controller;

import com.growup.pms.common.storage.service.StorageService;
import com.growup.pms.user.dto.UserCreateRequest;
import com.growup.pms.user.dto.UserUploadRequest;
import com.growup.pms.user.service.UserService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserControllerV1 {
    private final UserService userService;
    private final StorageService storageService;

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.created(URI.create("/api/v1/users/" + userService.save(request))).build();
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> upload(UserUploadRequest request) {
        MultipartFile file = request.getFile();
        String path = "users";

        UUID uploadFileName = storageService.upload(file, path);

        return ResponseEntity.ok().build();
    }
}
