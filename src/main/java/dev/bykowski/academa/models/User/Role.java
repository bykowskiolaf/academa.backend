/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-11-06
 * File: Role.java
 *
 * Last modified: 2024-11-06 17:07:22
 */

package dev.bykowski.academa.models.User;

import lombok.Getter;

@Getter
public enum Role {
    STUDENT("STUDENT"),
    INSTRUCTOR("INSTRUCTOR"),
    ADMIN("ADMIN");

    public static final String ADMIN_ROLE = "ADMIN";

    private final String role;

    Role(String role) {
        this.role = role;
    }
}
