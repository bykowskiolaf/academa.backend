/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-10-16
 * File: CreateUserDTO.java
 *
 * Last modified: 2024-10-16 20:24:25
 */

package dev.bykowski.academa.dtos.User;

import dev.bykowski.academa.models.User.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {
    private String email;

    private String username;

    private String givenName;

    private String familyName;

    private String picture;

    private String locale;

    private Set<Role> roles;
}
