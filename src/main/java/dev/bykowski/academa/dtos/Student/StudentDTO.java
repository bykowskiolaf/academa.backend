package dev.bykowski.academa.dtos.Student;

import dev.bykowski.academa.dtos.User.UserDTO;
import dev.bykowski.academa.models.Student;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentDTO extends UserDTO {
    private String studentId;
    private String studentClass;

    public StudentDTO(Student student) {
        super(student.getUuid(), student.getEmail(), student.getName(), student.getRole());
        this.studentId = student.getStudentId();
        this.studentClass = student.getStudentClass();
    }
}
