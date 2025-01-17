package com.growup.pms.role.domain;

public interface BasePermission {

    String getName();

    String getPrefix();

    default String getFullName() {
        return getPrefix() + getName();
    }
}
