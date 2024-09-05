package com.growup.pms.role.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@IdClass(RolePermissionId.class)
@Table(name = "role_permissions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RolePermission {

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_role_permission_role"))
    private Role role;

    @Id
    @ManyToOne
    @JoinColumn(name = "permission_id", foreignKey = @ForeignKey(name = "fk_role_permission_permission"))
    private Permission permission;

    public RolePermission(Role role, Permission permission) {
        this.role = role;
        this.permission = permission;
    }
}
