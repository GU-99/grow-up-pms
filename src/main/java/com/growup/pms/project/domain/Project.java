package com.growup.pms.project.domain;

import com.growup.pms.common.BaseEntity;
import com.growup.pms.team.domain.Team;
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
@Table(name = "projects")
@SQLDelete(sql = "UPDATE projects SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false, foreignKey = @ForeignKey(name = "fk_project_team"))
    private Team team;

    @Column(nullable = false)
    private String name;

    private String content;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder
    public Project(Team team, String name, String content, LocalDate startDate, LocalDate endDate) {
        this.team = team;
        this.name = name;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Business Logics //
    public void editName(String name) {
        this.name = name;
    }

    public void editContent(String content) {
        this.content = content;
    }

    public void editStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void editEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
