package com.growup.pms.team.domain;

import com.growup.pms.common.BaseEntity;
import com.growup.pms.role.domain.Role;
import com.growup.pms.user.domain.User;
import jakarta.persistence.Column;
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
@IdClass(TeamUserId.class)
@Table(name = "team_users")
@SQLDelete(sql = "UPDATE team_users SET is_deleted = true WHERE team_id = ? AND user_id = ?")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamUser extends BaseEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false, foreignKey = @ForeignKey(name = "fk_team_user_team"))
    private Team team;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_team_user_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "fk_team_user_role"))
    private Role role;

    @Column(nullable = false)
    private boolean isPendingApproval;

    @Builder
    public TeamUser(Team team, User user, Role role, boolean isPendingApproval) {
        this.team = team;
        this.user = user;
        this.role = role;
        this.isPendingApproval = isPendingApproval;
    }
}
