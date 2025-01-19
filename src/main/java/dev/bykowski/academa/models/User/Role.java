/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-11-09
 * File: Role.java
 *
 * Last modified: 2024-11-09 13:55:26
 */

package dev.bykowski.academa.models.User;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ADMIN"),
    MODERATOR("INSTRUCTOR"),
    STUDENT("STUDENT");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}