/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-11-06
 * File: Student.java
 *
 * Last modified: 2024-11-06 17:26:23
 */

package dev.bykowski.academa.models;

import dev.bykowski.academa.models.Course.Course;
import dev.bykowski.academa.models.User.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Student extends User {

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_uuid"),
            inverseJoinColumns = @JoinColumn(name = "course_uuid"))
    private Set<Course> studentCourses;
}
