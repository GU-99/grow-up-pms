package com.growup.pms.file.controller;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.common.aop.annotation.CurrentUser;
import com.growup.pms.common.aop.annotation.ProjectId;
import com.growup.pms.common.aop.annotation.RequireProjectPermission;
import com.growup.pms.common.util.FileNameUtil;
import com.growup.pms.common.validator.annotation.File;
import com.growup.pms.file.controller.dto.response.ProfileImageUpdateResponse;
import com.growup.pms.file.domain.FileType;
import com.growup.pms.file.service.ProfileImageService;
import com.growup.pms.file.service.TaskAttachmentService;
import com.growup.pms.role.domain.ProjectPermission;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FileControllerV1 {

    private final ProfileImageService profileImageService;
    private final TaskAttachmentService taskAttachmentService;

    @PostMapping("/user/profile/image")
    public ResponseEntity<ProfileImageUpdateResponse> uploadProfileImage(
            @CurrentUser SecurityUser user,
            @Valid @File(types = FileType.IMAGE) @RequestPart(name = "file") MultipartFile file
    ) {
        return ResponseEntity.ok(profileImageService.update(user.getId(), file));
    }

    @DeleteMapping("/user/profile/image")
    public ResponseEntity<Void> deleteProfileImage(@CurrentUser SecurityUser user) {
        profileImageService.delete(user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/file/profile/{fileName}")
    public ResponseEntity<byte[]> downloadProfileImage(@PathVariable String fileName) {
        if (!FileNameUtil.isValidFileName(fileName)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profileImageService.download(fileName));
    }

    /**
     * Uploads an attachment to a specific task within a project.
     *
     * @param projectId The ID of the project containing the task. Must be a positive number.
     * @param taskId The ID of the task to which the attachment will be added. Must be a positive number.
     * @param file The file to be uploaded. Must be an image, document, or archive file.
     * @return A ResponseEntity with an OK status upon successful file upload.
     * @throws ValidationException If the file does not meet the specified type constraints.
     * @throws AccessDeniedException If the user lacks permission to update the task.
     */
    @PostMapping("/project/{projectId}/task/{taskId}/upload")
    @RequireProjectPermission(ProjectPermission.UPDATE_TASK)
    public ResponseEntity<Void> uploadTaskAttachment(
            @Positive @PathVariable @ProjectId Long projectId,
            @Positive @PathVariable Long taskId,
            @Valid @File(types = {FileType.IMAGE, FileType.DOCUMENT, FileType.ARCHIVE}) @RequestPart(name = "file") MultipartFile file
    ) {
        taskAttachmentService.upload(taskId, file);
        return ResponseEntity.ok().build();
    }

    /**
     * Downloads a task attachment file for a specific task within a project.
     *
     * @param projectId The unique identifier of the project containing the task
     * @param taskId The unique identifier of the task associated with the attachment
     * @param fileName The name of the file to be downloaded
     * @return A ResponseEntity containing the file's byte content if the file is valid, or a not found response
     * @throws IllegalArgumentException if the task ID or file name is invalid
     */
    @GetMapping("/file/project/{projectId}/task/{taskId}/{fileName}")
    public ResponseEntity<byte[]> downloadTaskAttachment(
            @Positive @PathVariable Long projectId,
            @Positive @PathVariable Long taskId,
            @PathVariable String fileName
    ) {
        if (!FileNameUtil.isValidFileName(fileName)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskAttachmentService.download(taskId, fileName));
    }

    /**
     * Deletes a specific task attachment from a task within a project.
     *
     * @param projectId The unique identifier of the project containing the task
     * @param taskId The unique identifier of the task from which the attachment will be deleted
     * @param taskAttachmentId The unique identifier of the task attachment to be deleted
     * @return A ResponseEntity with no content, indicating successful deletion
     */
    @DeleteMapping("/project/{projectId}/task/{taskId}/file/{taskAttachmentId}")
    public ResponseEntity<Void> deleteTaskAttachment(
            @Positive @PathVariable Long projectId,
            @Positive @PathVariable Long taskId,
            @Positive @PathVariable Long taskAttachmentId
    ) {
        taskAttachmentService.deleteTaskAttachment(taskId, taskAttachmentId);
        return ResponseEntity.noContent().build();
    }
}
