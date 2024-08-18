package com.growup.pms.task.domain;

import com.growup.pms.common.BaseEntity;
import com.growup.pms.status.domain.Status;
import com.growup.pms.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Table(name = "status_tasks")
@SQLDelete(sql = "UPDATE status_tasks SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_status_id", foreignKey = @ForeignKey(name = "fk_task_status"))
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", foreignKey = @ForeignKey(name = "fk_task_user"))
    private User user;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false)
    private Short sortOrder;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder
    public Task(Status status, User user, String name, String content, Short sortOrder, LocalDate startDate,
                LocalDate endDate) {
        this.status = status;
        this.user = user;
        this.name = name;
        this.content = content;
        this.sortOrder = sortOrder;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
