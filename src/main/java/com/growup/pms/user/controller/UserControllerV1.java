package com.growup.pms.user.controller;

import com.growup.pms.user.dto.UserCreateRequest;
import com.growup.pms.user.dto.UserUploadRequest;
import com.growup.pms.user.service.UserService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserControllerV1 {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.created(URI.create("/api/v1/users/" + userService.save(request))).build();
    }

    @PostMapping("/file")
    public ResponseEntity<Void> upload(UserUploadRequest request) {
        userService.imageUpload(request);
        return ResponseEntity.ok().build();
    }
}
