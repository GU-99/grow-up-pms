package com.growup.pms.task.repository;

import com.growup.pms.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskQueryRepository {
}
