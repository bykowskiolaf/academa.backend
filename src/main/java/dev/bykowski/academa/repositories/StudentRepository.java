/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-10-27
 * File: StudentRepository.java
 *
 * Last modified: 2024-10-27 23:05:42
 */

package dev.bykowski.academa.repositories;

import dev.bykowski.academa.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);
}

