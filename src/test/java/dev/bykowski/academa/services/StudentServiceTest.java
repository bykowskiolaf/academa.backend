package dev.bykowski.academa.services;

import dev.bykowski.academa.dtos.Student.CreateStudentDTO;
import dev.bykowski.academa.dtos.Student.StudentDTO;
import dev.bykowski.academa.exceptions.AlreadyExistsException;
import dev.bykowski.academa.exceptions.NotFoundException;
import dev.bykowski.academa.models.Course.Course;
import dev.bykowski.academa.models.Student;
import dev.bykowski.academa.models.User.Role;
import dev.bykowski.academa.repositories.CourseRepository;
import dev.bykowski.academa.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    private UUID studentUuid;

    private UUID courseUuid;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        studentUuid = UUID.randomUUID();
        courseUuid = UUID.randomUUID();
    }

    @Test
    void shouldCreateStudent() {
        CreateStudentDTO createStudentDTO = CreateStudentDTO.builder()
                .email("student@example.com")
                .password("password123")
                .givenName("John")
                .familyName("Doe")
                .role("STUDENT")
                .studentCourses(Collections.singleton(courseUuid))
                .build();

        Course course = Course.builder()
                .uuid(courseUuid)
                .build();

        when(studentRepository.existsByEmail(createStudentDTO.getEmail())).thenReturn(false);
        when(courseRepository.findById(courseUuid)).thenReturn(Optional.of(course));

        Student student = Student.builder()
                .uuid(studentUuid)
                .email(createStudentDTO.getEmail())
                .givenName(createStudentDTO.getGivenName())
                .familyName(createStudentDTO.getFamilyName())
                .role(Role.STUDENT)
                .courses(Set.of(course))
                .build();

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDTO createdStudent = studentService.create(createStudentDTO);

        assertNotNull(createdStudent);
        assertEquals(createStudentDTO.getEmail(), createdStudent.getEmail());
        assertEquals(createStudentDTO.getGivenName(), createdStudent.getGivenName());
        assertEquals(createStudentDTO.getFamilyName(), createdStudent.getFamilyName());

        verify(studentRepository, times(1)).existsByEmail(createStudentDTO.getEmail());
        verify(courseRepository, times(1)).findById(courseUuid);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void shouldNotCreateStudentIfEmailExists() {
        CreateStudentDTO createStudentDTO = CreateStudentDTO.builder()
                .email("student@example.com")
                .build();

        when(studentRepository.existsByEmail(createStudentDTO.getEmail())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> studentService.create(createStudentDTO));

        verify(studentRepository, times(1)).existsByEmail(createStudentDTO.getEmail());
        verifyNoInteractions(courseRepository);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void shouldUpdateStudent() {
        CreateStudentDTO updateStudentDTO = CreateStudentDTO.builder()
                .email("updated@example.com")
                .givenName("Jane")
                .familyName("Doe")
                .role("STUDENT")
                .build();

        Student existingStudent = Student.builder()
                .uuid(studentUuid)
                .email("student@example.com")
                .givenName("John")
                .familyName("Smith")
                .role(Role.STUDENT)
                .build();

        Student updatedStudent = Student.builder()
                .uuid(studentUuid)
                .email(updateStudentDTO.getEmail())
                .givenName(updateStudentDTO.getGivenName())
                .familyName(updateStudentDTO.getFamilyName())
                .role(Role.STUDENT)
                .build();

        when(studentRepository.findById(studentUuid)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(existingStudent)).thenReturn(updatedStudent);

        StudentDTO updated = studentService.update(studentUuid, updateStudentDTO);

        assertNotNull(updated);
        assertEquals(updateStudentDTO.getEmail(), updated.getEmail());
        assertEquals(updateStudentDTO.getGivenName(), updated.getGivenName());
        assertEquals(updateStudentDTO.getFamilyName(), updated.getFamilyName());

        verify(studentRepository, times(1)).findById(studentUuid);
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonexistentStudent() {
        CreateStudentDTO updateStudentDTO = CreateStudentDTO.builder().build();

        when(studentRepository.findById(studentUuid)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> studentService.update(studentUuid, updateStudentDTO));

        verify(studentRepository, times(1)).findById(studentUuid);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void shouldDeleteStudent() {
        doNothing().when(studentRepository).deleteById(studentUuid);

        studentService.delete(studentUuid);

        verify(studentRepository, times(1)).deleteById(studentUuid);
    }

    @Test
    void shouldGetAllStudents() {
        Student student = Student.builder()
                .uuid(studentUuid)
                .email("student@example.com")
                .role(Role.STUDENT)
                .build();

        when(studentRepository.findAll()).thenReturn(Collections.singletonList(student));

        List<StudentDTO> students = studentService.getAll();

        assertNotNull(students);
        assertEquals(1, students.size());
        assertEquals(student.getEmail(), students.get(0).getEmail());

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void shouldGetStudentByUuid() {
        Student student = Student.builder()
                .uuid(studentUuid)
                .email("student@example.com")
                .role(Role.STUDENT)
                .build();

        when(studentRepository.findById(studentUuid)).thenReturn(Optional.of(student));

        StudentDTO foundStudent = studentService.getByUuid(studentUuid);

        assertNotNull(foundStudent);
        assertEquals(student.getEmail(), foundStudent.getEmail());

        verify(studentRepository, times(1)).findById(studentUuid);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGettingNonexistentStudent() {
        when(studentRepository.findById(studentUuid)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> studentService.getByUuid(studentUuid));

        verify(studentRepository, times(1)).findById(studentUuid);
    }
}