/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-11-09
 * File: RegisterUserDTO.java
 *
 * Last modified: 2024-11-09 14:52:45
 */

package dev.bykowski.academa.dtos.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {

    @NotNull(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    private String givenName;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Confirm password is required")
    private String confirmPassword;

    private String familyName;

    private String picture;

    private String locale;
}
