/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-10-23
 * File: UserDTO.java
 *
 * Last modified: 2024-10-23 16:37:12
 */

package dev.bykowski.academa.dtos.User;

import dev.bykowski.academa.models.User.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID uuid;

    private String email;

    private String userName;

    private String givenName;

    private String familyName;

    private String picture;

    private String locale;

    private Set<Role> roles;
}
