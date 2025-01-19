package dev.bykowski.academa.dtos.Course;

import dev.bykowski.academa.models.Course.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private UUID uuid;

    private String name;

    private String shortDescription;

    private String picture;

    public CourseDTO(Course course) {
        this.uuid = course.getUuid();
        this.name = course.getName();
        this.shortDescription = course.getShortDescription();
        this.picture = course.getPicture();
    }

    public CourseDTO mapToDTO(Course course) {
        return new CourseDTO(course);
    }
}
