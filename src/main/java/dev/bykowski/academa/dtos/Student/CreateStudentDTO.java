/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-10-16
 * File: CreateStudentDTO.java
 *
 * Last modified: 2024-10-16 23:38:55
 */

package dev.bykowski.academa.dtos.Student;

import dev.bykowski.academa.dtos.User.CreateUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateStudentDTO extends CreateUserDTO {

    private String studentId;

    private String studentClass;
}
