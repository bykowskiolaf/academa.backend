/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-10-31
 * File: Course.java
 *
 * Last modified: 2024-10-31 17:18:49
 */

package dev.bykowski.academa.models.Course;

import dev.bykowski.academa.models.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    private UUID uuid = UUID.randomUUID();

    @NotNull(message = "Course name cannot be null")
    private String name;

    private String description;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "studentCourses")
    private Set<Student> students;

}
