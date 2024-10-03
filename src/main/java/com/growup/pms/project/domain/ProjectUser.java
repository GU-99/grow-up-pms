package com.growup.pms.project.domain;

import com.growup.pms.common.BaseEntity;
import com.growup.pms.role.domain.Role;
import com.growup.pms.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@IdClass(ProjectUserId.class)
@Table(name = "project_users")
@SQLDelete(sql = "UPDATE project_users SET is_deleted = true WHERE project_id = ? AND user_id = ?")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectUser extends BaseEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, foreignKey = @ForeignKey(name = "fk_project_user_project"))
    private Project project;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_project_user_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "fk_project_user_role"))
    private Role role;

    @Builder
    public ProjectUser(Project project, User user, Role role) {
        this.project = project;
        this.user = user;
        this.role = role;
    }

    // Business Logics //
    public void changeRole(Role role) {
        this.role = role;
    }
}
