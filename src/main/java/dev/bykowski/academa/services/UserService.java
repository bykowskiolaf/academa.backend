/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-11-09
 * File: UserService.java
 *
 * Last modified: 2024-11-09 15:05:33
 */

package dev.bykowski.academa.services;

import dev.bykowski.academa.dtos.User.RegisterUserDTO;
import dev.bykowski.academa.dtos.User.UserDTO;
import dev.bykowski.academa.models.User.Role;
import dev.bykowski.academa.models.User.User;
import dev.bykowski.academa.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserDTO registerUser(RegisterUserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);

        User user = User.builder()
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .givenName(userDTO.getGivenName())
                .familyName(userDTO.getFamilyName())
                .picture(userDTO.getPicture())
                .locale(userDTO.getLocale())
                .role(Role.STUDENT)
                .build();

        return UserDTO.from(userRepository.save(user));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
