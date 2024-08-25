package com.growup.pms.role.domain;

import com.growup.pms.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Table(name = "permissions")
@SQLDelete(sql = "UPDATE permissions SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Permission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<RolePermission> rolePermissions = new ArrayList<>();

    public Permission(String name) {
        this.name = name;
    }
}
