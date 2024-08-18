package com.growup.pms.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
    @Column(nullable = false)
    private boolean isDeleted;
}
