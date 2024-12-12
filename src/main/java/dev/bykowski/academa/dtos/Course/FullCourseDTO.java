package dev.bykowski.academa.dtos.Course;

import dev.bykowski.academa.dtos.User.UserDTO;
import dev.bykowski.academa.models.Course.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullCourseDTO extends CourseDTO {
    private String longDescription;

    private UserDTO instructor;

    public FullCourseDTO(Course course) {
        super(course);
        this.longDescription = course.getLongDescription();
        this.instructor = new UserDTO(course.getInstructor());
    }

    public FullCourseDTO mapToDTO(Course course) {
        return new FullCourseDTO(course);
    }

}
