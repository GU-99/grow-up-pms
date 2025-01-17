package com.growup.pms.role.domain;

public interface BasePermission {

    /**
 * Retrieves the name of the permission.
 *
 * @return A {@code String} representing the specific name of the permission.
 */
String getName();

    /**
 * Retrieves the prefix associated with a permission.
 *
 * @return A {@code String} representing the prefix of the permission.
 */
String getPrefix();

    /**
     * Generates a full permission name by concatenating the permission prefix and name.
     *
     * @return A {@code String} representing the complete permission identifier, 
     *         formed by combining the prefix and name
     */
    default String getFullName() {
        return getPrefix() + getName();
    }
}
