package dev.bykowski.academa.dtos.Course;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseDTO {

    @NotBlank(message = "Course name cannot be blank")
    private String name;

    private String description;
}
