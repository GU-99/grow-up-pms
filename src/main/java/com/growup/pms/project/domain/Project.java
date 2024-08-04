package com.growup.pms.project.domain;

import com.growup.pms.common.BaseTimeEntity;
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
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
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
}
