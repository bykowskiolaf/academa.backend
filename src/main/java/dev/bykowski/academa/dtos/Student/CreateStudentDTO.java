package dev.bykowski.academa.dtos.Student;

import dev.bykowski.academa.dtos.User.CreateUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateStudentDTO extends CreateUserDTO {
    private String studentId;
    private String studentClass;
}
