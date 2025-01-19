package dev.bykowski.academa.dtos.Course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseDTO {

    @Size(max = 32, message = "Course name cannot be longer than 32 characters")
    @NotBlank(message = "Course name cannot be blank")
    private String name;

    @Size(max = 64, message = "Course short description cannot be longer than 255 characters")
    private String shortDescription;

    @Size(max = 2048, message = "Course long description cannot be longer than 512 characters")
    private String longDescription;

    private String picture;
}
