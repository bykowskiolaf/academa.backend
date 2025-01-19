/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-10-27
 * File: CreateUserDTO.java
 *
 * Last modified: 2024-10-27 23:37:15
 */

package dev.bykowski.academa.dtos.User;

import dev.bykowski.academa.models.User.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @NotNull(message = "Username is required")
    private String userName;

    private String givenName;

    private String familyName;

    private String picture;

    private String locale;

    @NotNull(message = "Roles are required")
    private Set<Role> roles;
}
