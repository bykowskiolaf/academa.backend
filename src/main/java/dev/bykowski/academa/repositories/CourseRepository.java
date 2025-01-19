/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-10-30
 * File: CourseRepository.java
 *
 * Last modified: 2024-10-30 17:03:07
 */

package dev.bykowski.academa.repositories;

import dev.bykowski.academa.models.Course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
}
