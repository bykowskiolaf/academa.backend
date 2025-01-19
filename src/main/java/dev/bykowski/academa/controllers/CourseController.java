/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-11-06
 * File: CourseController.java
 *
 * Last modified: 2024-11-06 16:48:52
 */

package dev.bykowski.academa.controllers;

import dev.bykowski.academa.dtos.Course.CourseDTO;
import dev.bykowski.academa.dtos.Course.CreateCourseDTO;
import dev.bykowski.academa.services.CourseService;
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
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {

        List<CourseDTO> courses = courseService.getAll();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CreateCourseDTO course) {
        CourseDTO savedCourse = courseService.createCourse(course);
        return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable UUID id) {
        CourseDTO course = courseService.getById(id);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable UUID id, @Valid @RequestBody CreateCourseDTO course) {
        CourseDTO updatedCourse = courseService.updateCourse(id, course);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID id) {
        courseService.deleteCourse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
