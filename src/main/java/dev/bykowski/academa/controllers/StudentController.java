/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-11-06
 * File: StudentController.java
 *
 * Last modified: 2024-11-06 17:07:22
 */

package dev.bykowski.academa.controllers;

import dev.bykowski.academa.dtos.Course.CourseDTO;
import dev.bykowski.academa.dtos.Student.CreateStudentDTO;
import dev.bykowski.academa.dtos.Student.StudentDTO;
import dev.bykowski.academa.exceptions.NotFoundException;
import dev.bykowski.academa.services.StudentService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {
    private StudentService studentService;

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody CreateStudentDTO studentDTO) {
        StudentDTO savedStudent = studentService.create(studentDTO);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    @GetMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAll();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    @RolesAllowed("INSTRUCTOR")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable UUID uuid) throws NotFoundException {
        StudentDTO student = studentService.getByUuid(uuid);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PutMapping("/{uuid}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable UUID uuid, @Valid @RequestBody CreateStudentDTO studentDTO) {
        StudentDTO updatedStudent = studentService.update(uuid, studentDTO);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }


    @DeleteMapping("/{uuid}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID uuid) {
        studentService.delete(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{studentUuid}/courses")
    @RolesAllowed("INSTRUCTOR")
    public ResponseEntity<List<CourseDTO>> getStudentCourses(@PathVariable UUID studentUuid) {
        List<CourseDTO> studentCourses = studentService.getStudentCourses(studentUuid);
        return new ResponseEntity<>(studentCourses, HttpStatus.OK);
    }

    @PostMapping("/{studentUuid}/courses/{courseUuid}")
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    public ResponseEntity<List<CourseDTO>> assignCourse(@PathVariable UUID studentUuid, @PathVariable UUID courseUuid) {
        List<CourseDTO> student = studentService.assignCourse(studentUuid, courseUuid);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @DeleteMapping("/{studentUuid}/courses/{courseUuid}")
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    public ResponseEntity<Void> unAssignStudent(@PathVariable UUID studentUuid, @PathVariable UUID courseUuid) {
        studentService.unassignCourse(studentUuid, courseUuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
