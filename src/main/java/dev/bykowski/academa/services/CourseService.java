/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-11-06
 * File: CourseService.java
 *
 * Last modified: 2024-11-06 16:26:10
 */

package dev.bykowski.academa.services;

import dev.bykowski.academa.dtos.Course.CourseDTO;
import dev.bykowski.academa.dtos.Course.CreateCourseDTO;
import dev.bykowski.academa.models.Course.Course;
import dev.bykowski.academa.repositories.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private CourseRepository courseRepository;

    public List<CourseDTO> getAll() {
        return courseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO getById(UUID id) {
        return courseRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public CourseDTO createCourse(CreateCourseDTO createCourseDTO) {
        System.out.println(createCourseDTO);
        Course course = Course.builder()
                .name(createCourseDTO.getName())
                .description(createCourseDTO.getDescription())
                .build();
        System.out.println(course);
        return mapToDTO(courseRepository.save(course));
    }

    public CourseDTO updateCourse(UUID id, CreateCourseDTO createCourseDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setName(createCourseDTO.getName());
        course.setDescription(createCourseDTO.getDescription());
        return mapToDTO(courseRepository.save(course));
    }

    public void deleteCourse(UUID id) {
        courseRepository.deleteById(id);
    }

    public CourseDTO mapToDTO(Course course) {
        return new CourseDTO(course);
    }
}
