package dev.bykowski.academa.controllers;

import dev.bykowski.academa.dtos.Student.CreateStudentDTO;
import dev.bykowski.academa.dtos.Student.StudentDTO;
import dev.bykowski.academa.exceptions.NotFoundException;
import dev.bykowski.academa.services.StudentService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
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

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody CreateStudentDTO studentDTO) {
        System.out.println(studentDTO);
        StudentDTO savedStudent = studentService.create(studentDTO);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    @GetMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAll();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    @RolesAllowed("INSTRUCTOR")
    public ResponseEntity<StudentDTO> getStudentByUuid(@PathVariable UUID uuid) throws NotFoundException {
        StudentDTO student = studentService.getByUuid(uuid);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PutMapping("/{uuid}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable UUID uuid, @Valid @RequestBody CreateStudentDTO studentDTO) {
        StudentDTO updatedStudent = studentService.update(uuid, studentDTO);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }


    @DeleteMapping("/{uuid}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID uuid) {
        studentService.delete(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
