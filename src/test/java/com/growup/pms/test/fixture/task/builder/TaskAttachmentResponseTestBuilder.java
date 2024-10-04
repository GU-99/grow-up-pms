package com.growup.pms.test.fixture.task.builder;

import com.growup.pms.task.controller.dto.response.TaskAttachmentResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskAttachmentResponseTestBuilder {

    private Long fileId = 1L;
    private String fileName = "pig.jpg";
    private String uploadName = "f34abb71-1788-41d7-9f1c-11f66b5a9896.png";

    public static TaskAttachmentResponseTestBuilder 첨부파일_조회_응답은() {
        return new TaskAttachmentResponseTestBuilder();
    }

    public TaskAttachmentResponseTestBuilder 첨부파일_식별자가(Long fileId) {
        this.fileId = fileId;
        return this;
    }

    public TaskAttachmentResponseTestBuilder 원본_파일명이(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public TaskAttachmentResponseTestBuilder 저장_파일명이(String uploadName) {
        this.uploadName = uploadName;
        return this;
    }

    public TaskAttachmentResponse 이다() {
        return TaskAttachmentResponse.builder()
                .fileId(fileId)
                .fileName(fileName)
                .uploadName(uploadName)
                .build();
    }
}
