package dev.bykowski.academa.controllers;

import dev.bykowski.academa.dtos.Student.CreateStudentDTO;
import dev.bykowski.academa.dtos.Student.StudentDTO;
import dev.bykowski.academa.exceptions.UserNotFoundException;
import dev.bykowski.academa.models.Student;
import dev.bykowski.academa.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {
    private StudentService studentService;

    // Create a new student
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody CreateStudentDTO studentDTO) {
        StudentDTO savedStudent = studentService.save(studentDTO);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    // Get all students
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAll();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Get student by UUID
    @GetMapping("/{uuid}")
    public StudentDTO getStudentById(@PathVariable UUID uuid) throws UserNotFoundException {
        Student student = studentService.getStudentById(uuid);
        return studentService.mapToDTO(student);
    }

}