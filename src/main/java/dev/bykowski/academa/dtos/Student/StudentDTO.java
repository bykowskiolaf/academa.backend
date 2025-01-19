/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-11-06
 * File: StudentDTO.java
 *
 * Last modified: 2024-11-06 16:23:27
 */

package dev.bykowski.academa.dtos.Student;

import dev.bykowski.academa.dtos.User.UserDTO;
import dev.bykowski.academa.models.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentDTO extends UserDTO {

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
    }

    public StudentDTO mapToDTO(Student student) {
        return new StudentDTO(student);
    }
}
