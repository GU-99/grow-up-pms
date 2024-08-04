package com.growup.pms.team.domain;

import com.growup.pms.common.BaseTimeEntity;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "teams", uniqueConstraints = @UniqueConstraint(columnNames = {"creator_id", "name"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", updatable = false, nullable = false, foreignKey = @ForeignKey(name = "fk_team_creator"))
    private User creator;

    @Column(nullable = false)
    private String name;

    private String content;

    @Builder
    public Team(User creator, String name, String content) {
        this.creator = creator;
        this.name = name;
        this.content = content;
    }

    public void updateName(String newName) {
        this.name = newName;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }
}
