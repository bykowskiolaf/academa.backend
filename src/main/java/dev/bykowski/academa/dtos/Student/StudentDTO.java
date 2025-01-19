package dev.bykowski.academa.dtos.Student;

import dev.bykowski.academa.dtos.User.UserDTO;
import dev.bykowski.academa.models.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentDTO extends UserDTO {

    public StudentDTO(Student student) {
        super(
                student.getUuid(),
                student.getEmail(),
                student.getGivenName(),
                student.getFamilyName(),
                student.getPicture(),
                student.getLocale(),
                student.getRole().getName()
        );
    }

}
