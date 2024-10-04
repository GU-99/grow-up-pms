package com.growup.pms.task.repository;

import static com.growup.pms.task.domain.QTask.task;
import static com.growup.pms.task.domain.QTaskAttachment.taskAttachment;

import com.growup.pms.task.controller.dto.response.TaskAttachmentResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TaskAttachmentQueryRepositoryImpl implements TaskAttachmentQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TaskAttachmentResponse> getTaskAttachmentsByTaskId(Long taskId) {
        return queryFactory.select(Projections.constructor(TaskAttachmentResponse.class,
                        taskAttachment.id,
                        taskAttachment.originalFileName,
                        taskAttachment.storeFileName
                ))
                .from(taskAttachment)
                .join(taskAttachment.task, task)
                .where(isTaskId(taskId))
                .fetch();
    }

    private BooleanExpression isTaskId(Long taskId) {
        return taskId != null ? taskAttachment.task.id.eq(taskId) : null;
    }
}
