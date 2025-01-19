/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-11-09
 * File: StudentService.java
 *
 * Last modified: 2024-11-09 14:48:07
 */

package dev.bykowski.academa.services;

import dev.bykowski.academa.dtos.Course.CourseDTO;
import dev.bykowski.academa.dtos.Student.CreateStudentDTO;
import dev.bykowski.academa.dtos.Student.StudentDTO;
import dev.bykowski.academa.exceptions.AlreadyExistsException;
import dev.bykowski.academa.exceptions.NotFoundException;
import dev.bykowski.academa.models.Course.Course;
import dev.bykowski.academa.models.Student;
import dev.bykowski.academa.models.User.Role;
import dev.bykowski.academa.repositories.CourseRepository;
import dev.bykowski.academa.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentService {
    private StudentRepository studentRepository;

    private CourseRepository courseRepository;

    public StudentDTO create(CreateStudentDTO studentDTO) {
        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new AlreadyExistsException(
                    String.format("Student with email %s already exists", studentDTO.getEmail())
            );
        }

        Set<Course> studentCourses = studentDTO.getStudentCourses().stream()
                .map(courseUuid -> courseRepository.findById(courseUuid).orElseThrow(() -> new NotFoundException(
                        String.format("Course with uuid %s not found", courseUuid)
                )))
                .collect(Collectors.toSet());

        Student student = Student.builder()
                .email(studentDTO.getEmail())
                .password(studentDTO.getPassword())
                .givenName(studentDTO.getGivenName())
                .familyName(studentDTO.getFamilyName())
                .picture(studentDTO.getPicture())
                .locale(studentDTO.getLocale())
                .role(Role.valueOf(studentDTO.getRole()))
                .courses(studentCourses)
                .build();

        return mapToDTO(studentRepository.save(student));
    }

    public StudentDTO update(UUID uuid, CreateStudentDTO studentDTO) {
        Student student = studentRepository.findById(uuid).orElseThrow(() -> new NotFoundException(
                String.format("Student with uuid %s not found", uuid)
        ));

        student.setEmail(studentDTO.getEmail());
        student.setGivenName(studentDTO.getGivenName());
        student.setFamilyName(studentDTO.getFamilyName());
        student.setPicture(studentDTO.getPicture());
        student.setLocale(studentDTO.getLocale());
        student.setRole(Role.valueOf(studentDTO.getRole()));

        return mapToDTO(studentRepository.save(student));
    }

    public void delete(UUID uuid) {
        studentRepository.deleteById(uuid);
    }

    public List<CourseDTO> assignCourse(UUID studentUuid, UUID courseUuid) {
        Student student = studentRepository.findById(studentUuid).orElseThrow(() -> new NotFoundException(
                String.format("Student with uuid %s not found", studentUuid)
        ));
        Course course = courseRepository.findById(courseUuid).orElseThrow(() -> new NotFoundException(
                String.format("Course with uuid %s not found", courseUuid)
        ));

        student.getCourses().add(course);

        course.getStudents().add(student);

        studentRepository.save(student);

        return student.getCourses().stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }

    public void unassignCourse(UUID studentUuid, UUID courseUuid) {
        Student student = studentRepository.findById(studentUuid).orElseThrow(() -> new NotFoundException(
                String.format("Student with uuid %s not found", studentUuid)
        ));
        Course course = courseRepository.findById(courseUuid).orElseThrow(() -> new NotFoundException(
                String.format("Course with uuid %s not found", courseUuid)
        ));

        student.getCourses().remove(course);

        course.getStudents().remove(student);

        studentRepository.save(student);
    }

    public List<StudentDTO> getAll() {
        List<Student> students = studentRepository.findAll();
        System.out.println(students);
        return students.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO getByUuid(UUID uuid) throws NotFoundException {
        return mapToDTO(studentRepository.findById(uuid).orElseThrow(() -> new NotFoundException(
                String.format("Student with uuid %s not found", uuid)
        )));
    }

    public List<CourseDTO> getStudentCourses(UUID studentUuid) {
        Student student = studentRepository.findById(studentUuid)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Student with uuid %s not found", studentUuid)
                ));
        return student.getCourses().stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }

    public StudentDTO mapToDTO(Student student) {
        return new StudentDTO(student);
    }

}
