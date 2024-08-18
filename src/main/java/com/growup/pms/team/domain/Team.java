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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Table(name = "teams", uniqueConstraints = @UniqueConstraint(columnNames = {"creator_id", "name"}))
@SQLDelete(sql = "UPDATE teams SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", updatable = false, nullable = false, foreignKey = @ForeignKey(name = "fk_team_creator"))
    private User creator;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(length = 300)
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
