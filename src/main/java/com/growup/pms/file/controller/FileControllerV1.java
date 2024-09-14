package com.growup.pms.file.controller;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.common.aop.annotation.CurrentUser;
import com.growup.pms.common.util.FileNameUtil;
import com.growup.pms.common.validator.annotation.File;
import com.growup.pms.file.domain.FileType;
import com.growup.pms.file.service.ProfileImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileControllerV1 {

    private final ProfileImageService profileImageService;

    @PostMapping("/user/profile/image")
    public ResponseEntity<Void> uploadProfileImage(
            @CurrentUser SecurityUser user,
            @Valid @File(types = FileType.IMAGE) @RequestPart(name = "file") MultipartFile file
    ) {
        profileImageService.update(user.getId(), file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/file/profile/{fileName}")
    public ResponseEntity<byte[]> downloadProfileImage(@PathVariable String fileName) {
        if (!FileNameUtil.isValidFileName(fileName)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profileImageService.download(fileName));
    }
}
