/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-11-06
 * File: User.java
 *
 * Last modified: 2024-11-06 17:27:08
 */

package dev.bykowski.academa.models.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "app_user")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    private UUID uuid = UUID.randomUUID();

    @NotNull(message = "Email cannot be null")
    @Email
    @Column(unique = true)
    private String email;

    private String givenName;

    private String familyName;

    private String picture;

    private String locale;


    @NotNull(message = "Username cannot be null")
    @Column(unique = true)
    private String userName;

    @NotNull(message = "Roles cannot be null")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();
}
