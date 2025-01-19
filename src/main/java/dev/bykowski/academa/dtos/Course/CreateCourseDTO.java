/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-10-30
 * File: CreateCourseDTO.java
 *
 * Last modified: 2024-10-30 17:07:25
 */

package dev.bykowski.academa.dtos.Course;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseDTO {

    @NotNull(message = "Course name cannot be null")
    private String name;

    private String description;
}
