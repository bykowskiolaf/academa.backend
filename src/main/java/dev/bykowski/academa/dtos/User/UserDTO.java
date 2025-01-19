/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-11-09
 * File: UserDTO.java
 *
 * Last modified: 2024-11-09 14:46:04
 */

package dev.bykowski.academa.dtos.User;

import dev.bykowski.academa.models.User.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID uuid;

    private String email;

    private String givenName;

    private String familyName;

    private String picture;

    private String locale;

    private String role;

    public static UserDTO from(User user) {
        return UserDTO.builder()
                .uuid(user.getUuid())
                .email(user.getEmail())
                .givenName(user.getGivenName())
                .familyName(user.getFamilyName())
                .picture(user.getPicture())
                .locale(user.getLocale())
                .role(user.getRole().getName())
                .build();
    }
}
