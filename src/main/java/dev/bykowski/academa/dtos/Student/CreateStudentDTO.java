package dev.bykowski.academa.dtos.Student;

import dev.bykowski.academa.dtos.User.CreateUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateStudentDTO extends CreateUserDTO {

    private Set<UUID> studentCourses;
}
