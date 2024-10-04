package com.growup.pms.task.domain;

import com.growup.pms.common.BaseEntity;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Table(name = "status_tasks_attachments")
@SQLDelete(sql = "UPDATE status_tasks_attachments SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskAttachment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_tasks_id", foreignKey = @ForeignKey(name = "fk_status_tasks"), nullable = false)
    private Task task;

    @Column(nullable = false)
    String originalFileName;

    @Column(nullable = false)
    String storeFileName;

    @Builder
    public TaskAttachment(Task task, String originalFileName, String storeFileName) {
        this.task = task;
        this.originalFileName = originalFileName;
        this.storeFileName = storeFileName;
    }
}
