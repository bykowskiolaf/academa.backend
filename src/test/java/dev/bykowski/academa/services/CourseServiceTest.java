package dev.bykowski.academa.services;

import dev.bykowski.academa.dtos.Course.CourseDTO;
import dev.bykowski.academa.dtos.Course.CreateCourseDTO;
import dev.bykowski.academa.exceptions.NotFoundException;
import dev.bykowski.academa.models.Course.Course;
import dev.bykowski.academa.models.Student;
import dev.bykowski.academa.models.User.User;
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

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllCourses() {
        Course course = Course.builder()
                .uuid(UUID.randomUUID())
                .name("Test Course")
                .shortDescription("Test short description")
                .longDescription("Test long description")
                .build();

        when(courseRepository.findAll()).thenReturn(Collections.singletonList(course));

        List<CourseDTO> courses = courseService.getAll();

        assertNotNull(courses);
        assertEquals(1, courses.size());
        assertEquals("Test Course", courses.getFirst().getName());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void shouldGetCourseByUuid() {
        UUID courseUuid = UUID.randomUUID();
        Course course = Course.builder()
                .uuid(courseUuid)
                .name("Test Course")
                .shortDescription("Test short description")
                .longDescription("Test long description")
                .build();

        when(courseRepository.findById(courseUuid)).thenReturn(Optional.of(course));

        CourseDTO courseDTO = courseService.getByUuid(courseUuid);

        assertNotNull(courseDTO);
        assertEquals("Test Course", courseDTO.getName());
        verify(courseRepository, times(1)).findById(courseUuid);
    }

    @Test
    void shouldThrowNotFoundWhenCourseNotFound() {
        UUID courseUuid = UUID.randomUUID();
        when(courseRepository.findById(courseUuid)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> courseService.getByUuid(courseUuid));
    }

    @Test
    void shouldCreateCourse() {
        UUID instructorUuid = UUID.randomUUID();
        CreateCourseDTO createCourseDTO = CreateCourseDTO.builder()
                .name("New Course")
                .shortDescription("New Description")
                .longDescription("New Long Description")
                .build();

        User instructor = User.builder()
                .uuid(instructorUuid)
                .build();

        Course course = Course.builder()
                .uuid(UUID.randomUUID())
                .name("New Course")
                .shortDescription("New Description")
                .longDescription("New Long Description")
                .instructor(instructor)
                .build();

        when(userService.getByUuid(instructorUuid)).thenReturn(instructor);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        CourseDTO courseDTO = courseService.createCourse(createCourseDTO, instructorUuid);

        assertNotNull(courseDTO);
        assertEquals("New Course", courseDTO.getName());
        verify(userService, times(1)).getByUuid(instructorUuid);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void shouldUpdateCourse() {
        UUID courseUuid = UUID.randomUUID();
        CreateCourseDTO createCourseDTO = CreateCourseDTO.builder()
                .name("Updated Course")
                .shortDescription("Updated Description")
                .longDescription("Updated Long Description")
                .build();

        Course course = Course.builder()
                .uuid(courseUuid)
                .name("Original Course")
                .shortDescription("Original Description")
                .build();

        when(courseRepository.findById(courseUuid)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(course);

        CourseDTO updatedCourseDTO = courseService.updateCourse(courseUuid, createCourseDTO);

        assertNotNull(updatedCourseDTO);
        assertEquals("Updated Course", updatedCourseDTO.getName());
        assertEquals("Updated Description", updatedCourseDTO.getShortDescription());

        verify(courseRepository, times(1)).findById(courseUuid);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void shouldDeleteCourse() {
        UUID courseUuid = UUID.randomUUID();

        doNothing().when(courseRepository).deleteById(courseUuid);

        courseService.deleteCourse(courseUuid);

        verify(courseRepository, times(1)).deleteById(courseUuid);
    }

    @Test
    void shouldAssignCourseToStudent() {
        UUID studentUuid = UUID.randomUUID();
        UUID courseUuid = UUID.randomUUID();

        Student student = Student.builder()
                .uuid(studentUuid)
                .courses(new HashSet<>())
                .build();

        Course course = Course.builder()
                .uuid(courseUuid)
                .students(new HashSet<>())
                .build();

        when(studentRepository.findById(studentUuid)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseUuid)).thenReturn(Optional.of(course));

        List<CourseDTO> courses = courseService.assignCourse(studentUuid, courseUuid);

        assertNotNull(courses);
        assertEquals(1, courses.size());
        verify(studentRepository, times(1)).findById(studentUuid);
        verify(courseRepository, times(1)).findById(courseUuid);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void shouldThrowNotFoundWhenAssigningToNonExistentStudent() {
        UUID studentUuid = UUID.randomUUID();
        UUID courseUuid = UUID.randomUUID();

        when(studentRepository.findById(studentUuid)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> courseService.assignCourse(studentUuid, courseUuid));
    }

    @Test
    void shouldUnassignCourseFromStudent() {
        UUID studentUuid = UUID.randomUUID();
        UUID courseUuid = UUID.randomUUID();

        Course course = Course.builder()
                .uuid(courseUuid)
                .build();

        Student student = Student.builder()
                .uuid(studentUuid)
                .courses(new HashSet<>(Collections.singleton(course)))
                .build();

        course.setStudents(new HashSet<>(Collections.singleton(student)));

        when(studentRepository.findById(studentUuid)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseUuid)).thenReturn(Optional.of(course));

        courseService.unassignCourse(studentUuid, courseUuid);

        verify(studentRepository, times(1)).save(student);
        assertTrue(student.getCourses().isEmpty());
        assertTrue(course.getStudents().isEmpty());
    }
}