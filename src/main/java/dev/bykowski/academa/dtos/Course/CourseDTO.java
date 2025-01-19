/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-10-30
 * File: CourseDTO.java
 *
 * Last modified: 2024-10-30 17:28:11
 */

package dev.bykowski.academa.dtos.Course;

import dev.bykowski.academa.models.Course.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private UUID uuid;

    private String name;

    private String description;

    public CourseDTO(Course course) {
        this.uuid = course.getUuid();
        this.name = course.getName();
        this.description = course.getDescription();
    }

    public CourseDTO mapToDTO(Course course) {
        return new CourseDTO(course);
    }
}
