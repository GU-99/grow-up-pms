package com.growup.pms.task.repository;

import com.growup.pms.task.domain.TaskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment, Long> {

}
