package com.growup.pms.team.domain;

import com.growup.pms.role.Role;
import com.growup.pms.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@IdClass(TeamUserId.class)
@Table(name = "team_users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamUser {
    @Id
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Builder
    public TeamUser(Team team, User user, Role role) {
        this.team = team;
        this.user = user;
        this.role = role;
    }
}
