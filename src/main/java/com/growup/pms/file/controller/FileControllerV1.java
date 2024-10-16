package com.growup.pms.file.controller;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.common.aop.annotation.CurrentUser;
import com.growup.pms.common.aop.annotation.ProjectId;
import com.growup.pms.common.aop.annotation.RequirePermission;
import com.growup.pms.common.util.FileNameUtil;
import com.growup.pms.common.validator.annotation.File;
import com.growup.pms.file.domain.FileType;
import com.growup.pms.file.service.ProfileImageService;
import com.growup.pms.file.service.TaskAttachmentService;
import com.growup.pms.role.domain.PermissionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private final TaskAttachmentService taskAttachmentService;

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

    @PostMapping("/project/{projectId}/task/{taskId}/upload")
    @RequirePermission(PermissionType.PROJECT_TASK_WRITE)
    public ResponseEntity<Void> uploadTaskAttachment(
            @Positive @ProjectId @PathVariable Long projectId,
            @Positive @PathVariable Long taskId,
            @Valid @File(types = {FileType.IMAGE, FileType.DOCUMENT, FileType.ARCHIVE}) @RequestPart(name = "file") MultipartFile file
    ) {
        taskAttachmentService.upload(taskId, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/file/project/{projectId}/task/{taskId}/{fileName}")
    public ResponseEntity<byte[]> downloadTaskAttachment(
            @Positive @ProjectId @PathVariable Long projectId,
            @Positive @PathVariable Long taskId,
            @PathVariable String fileName
    ) {
        if (!FileNameUtil.isValidFileName(fileName)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskAttachmentService.download(taskId, fileName));
    }

    @DeleteMapping("/project/{projectId}/task/{taskId}/file/{taskAttachmentId}")
    public ResponseEntity<Void> deleteTaskAttachment(
            @Positive @ProjectId @PathVariable Long projectId,
            @Positive @PathVariable Long taskId,
            @Positive @PathVariable Long taskAttachmentId
    ) {
        taskAttachmentService.deleteTaskAttachment(taskId, taskAttachmentId);
        return ResponseEntity.noContent().build();
    }
}
