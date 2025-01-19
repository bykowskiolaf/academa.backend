/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-10-23
 * File: StudentDTO.java
 *
 * Last modified: 2024-10-23 17:26:10
 */

package dev.bykowski.academa.dtos.Student;

import dev.bykowski.academa.dtos.User.UserDTO;
import dev.bykowski.academa.models.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentDTO extends UserDTO {
    private HashSet<String> studentClasses;

    public StudentDTO(Student student) {
        super(
                student.getUuid(),
                student.getEmail(),
                student.getGivenName(),
                student.getGivenName(),
                student.getFamilyName(),
                student.getPicture(),
                student.getLocale(),
                student.getRoles()
        );
        this.studentClasses = student.getStudentClasses();
    }
}
