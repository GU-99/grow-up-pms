package com.growup.pms.file.service;

import com.growup.pms.task.domain.Task;
import com.growup.pms.task.domain.TaskAttachment;
import com.growup.pms.task.repository.TaskAttachmentRepository;
import com.growup.pms.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskAttachmentService {

    private final TaskRepository taskRepository;
    private final TaskAttachmentRepository taskAttachmentRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public void upload(Long taskId, MultipartFile file) {
        Task task = taskRepository.findByIdOrThrow(taskId);
        String storeFileName = fileStorageService.upload(file);
        createTaskAttachment(file, task, storeFileName);
    }

    @Transactional(propagation = Propagation.NEVER)
    public byte[] download(Long taskId, String fileName) {
        Task task = taskRepository.findByIdOrThrow(taskId);
        return fileStorageService.download(fileName);
    }

    @Transactional
    public void deleteTaskAttachment(Long taskId, Long taskAttachmentId) {
        Task  task = taskRepository.findByIdOrThrow(taskId);
        TaskAttachment taskAttachment = taskAttachmentRepository.findByIdOrThrow(taskAttachmentId);
        taskAttachmentRepository.delete(taskAttachment);
    }

    private void createTaskAttachment(MultipartFile file, Task task, String storeFileName) {
        TaskAttachment taskAttachment = TaskAttachment.builder()
                .task(task)
                .originalFileName(file.getOriginalFilename())
                .storeFileName(storeFileName)
                .build();
        taskAttachmentRepository.save(taskAttachment);
    }
}
