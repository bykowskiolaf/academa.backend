/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-11-06
 * File: UserRegistrationDTO.java
 *
 * Last modified: 2024-11-06 17:36:11
 */

package dev.bykowski.academa.dtos.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationDTO {
    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    @Size(min = 8)
    private String password;

    @NotNull
    @Email
    private String email;
}
