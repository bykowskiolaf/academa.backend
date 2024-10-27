/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-10-23
 * File: StudentService.java
 *
 * Last modified: 2024-10-23 17:18:31
 */

package dev.bykowski.academa.services;

import dev.bykowski.academa.dtos.Student.CreateStudentDTO;
import dev.bykowski.academa.dtos.Student.StudentDTO;
import dev.bykowski.academa.exceptions.UserAlreadyExistsException;
import dev.bykowski.academa.exceptions.UserNotFoundException;
import dev.bykowski.academa.models.Student;
import dev.bykowski.academa.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentService {
    private StudentRepository studentRepository;

    public StudentDTO save(CreateStudentDTO studentDTO) {
        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new UserAlreadyExistsException(
                    String.format("Student with email %s already exists", studentDTO.getEmail())
            );
        }

        if (studentRepository.existsByUserName(studentDTO.getUserName())) {
            throw new UserAlreadyExistsException(
                    String.format("Student with username %s already exists", studentDTO.getUserName())
            );
        }

        Student student = Student.builder()
                .email(studentDTO.getEmail())
                .givenName(studentDTO.getGivenName())
                .familyName(studentDTO.getFamilyName())
                .picture(studentDTO.getPicture())
                .locale(studentDTO.getLocale())
                .roles(studentDTO.getRoles())
                .build();
        return mapToDTO(studentRepository.save(student));
    }

    public List<StudentDTO> getAll() {
        return studentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Student getStudentById(UUID uuid) throws UserNotFoundException {
        return studentRepository.findById(uuid).orElseThrow(() -> new UserNotFoundException(
                String.format("Student with uuid %s not found", uuid)
        ));
    }

    public StudentDTO mapToDTO(Student student) {
        return new StudentDTO(student);
    }

}
