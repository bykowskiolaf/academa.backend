/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-10-23
 * File: CustomOAuth2UserService.java
 *
 * Last modified: 2024-10-23 17:32:39
 */

package dev.bykowski.academa.services;

import dev.bykowski.academa.models.Student;
import dev.bykowski.academa.models.User.Role;
import dev.bykowski.academa.models.User.User;
import dev.bykowski.academa.repositories.StudentRepository;
import dev.bykowski.academa.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private UserRepository userRepository;

    private StudentRepository studentRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(userRequest);

        log.debug("OAuth2User: {}", oauthUser.getAttributes());
        return processOAuth2User(oauthUser);
    }

    private OAuth2User processOAuth2User(OAuth2User oauthUser) {
        String email = oauthUser.getAttribute("email");
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            log.debug("User not found with email: {}. Creating a new user.", email);
            assert email != null;
            Student newStudent = Student.builder()
                    .email(email)
                    .userName(Objects.requireNonNull(oauthUser.getAttribute("name")))
                    .givenName(Objects.requireNonNull(oauthUser.getAttribute("given_name")))
                    .familyName(Objects.requireNonNull(oauthUser.getAttribute("family_name")))
                    .picture(oauthUser.getAttribute("picture"))
                    .locale(oauthUser.getAttribute("locale"))
                    .roles(new HashSet<>(Set.of(Role.STUDENT)))
                    .studentClasses(new HashSet<>())
                    .build();

            studentRepository.save(newStudent);
            user = newStudent;
        }

        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());

        return new DefaultOAuth2User(authorities, oauthUser.getAttributes(), "email");
    }
}