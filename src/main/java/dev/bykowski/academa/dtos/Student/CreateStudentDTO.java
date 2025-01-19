/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-11-09
 * File: CreateStudentDTO.java
 *
 * Last modified: 2024-11-09 13:14:09
 */

package dev.bykowski.academa.dtos.Student;

import dev.bykowski.academa.dtos.User.CreateUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateStudentDTO extends CreateUserDTO {

    private Set<UUID> studentCourses;
}
