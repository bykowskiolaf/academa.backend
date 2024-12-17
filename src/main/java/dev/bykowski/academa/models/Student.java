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
    private Set<Course> enrolledCourses;
}
