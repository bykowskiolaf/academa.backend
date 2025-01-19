package dev.bykowski.academa.services;

import dev.bykowski.academa.dtos.Student.CreateStudentDTO;
import dev.bykowski.academa.dtos.Student.StudentDTO;
import dev.bykowski.academa.dtos.User.UserDTO;
import dev.bykowski.academa.exceptions.UserNotFoundException;
import dev.bykowski.academa.models.Student;
import dev.bykowski.academa.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentService {
    private StudentRepository studentRepository;

    public StudentDTO save(CreateStudentDTO studentDTO) {
        Student student = Student.builder()
                .email(studentDTO.getEmail())
                .name(studentDTO.getName())
                .role(studentDTO.getRole())
                .studentId(studentDTO.getStudentId())
                .studentClass(studentDTO.getStudentClass())
                .build();
        return mapToDTO(studentRepository.save(student));
    }

    public List<StudentDTO> getAll() {
        return studentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Student getStudentById(UUID uuid) throws UserNotFoundException {
        return studentRepository.findById(uuid).orElseThrow(() -> new UserNotFoundException(
                String.format("Student with uuid %s not found", uuid)
        ));
    }

    public StudentDTO mapToDTO(Student student) {
        return new StudentDTO(student);
    }

    public Student mapToEntity(UserDTO userDTO) {
        return Student.builder()
                .uuid(userDTO.getUuid())
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .role(userDTO.getRole())
                .build();
    }
}
