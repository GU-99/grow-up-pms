package com.growup.pms.test.fixture.task.builder;

import static com.growup.pms.test.fixture.task.builder.TaskTestBuilder.일정은;

import com.growup.pms.task.domain.Task;
import com.growup.pms.task.domain.TaskAttachment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskAttachmentTestBuilder {

    private Long id = 1L;
    private Task task = 일정은().이다();
    private String originalFileName = "pig.jpg";
    private String storeFileName = "f34abb71-1788-41d7-9f1c-11f66b5a9896.png";

    public static TaskAttachmentTestBuilder 일정_첨부파일은() {
        return new TaskAttachmentTestBuilder();
    }

    public TaskAttachmentTestBuilder 첨부파일_식별자가(Long id) {
        this.id = id;
        return this;
    }

    public TaskAttachmentTestBuilder 일정이(Task task) {
        this.task = task;
        return this;
    }

    public TaskAttachmentTestBuilder 원본파일명이(String originalFileName) {
        this.originalFileName = originalFileName;
        return this;
    }

    public TaskAttachmentTestBuilder 저장파일명이(String storeFileName) {
        this.storeFileName = storeFileName;
        return this;
    }

    public TaskAttachment 이다() {
        var builder = TaskAttachment.builder()
                .task(task)
                .originalFileName(originalFileName)
                .storeFileName(storeFileName)
                .build();
        ReflectionTestUtils.setField(builder, "id", id);
        return builder;
    }
}
